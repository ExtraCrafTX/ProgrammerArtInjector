package com.extracraftx.minecraft.programmerartinjector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.ZipResourcePack;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.resource.metadata.PackResourceMetadataReader;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class ProgrammerArtResourcePack extends ZipResourcePack {

	private static final Pattern RESOURCE_PACK_PATH = Pattern.compile("[a-z0-9-_]+");

    public ProgrammerArtResourcePack(File file) {
        super(file);
    }

    @Override
    public String getName() {
        return "Programmer Art";
    }

    @Override
    public boolean contains(ResourceType resourceType, Identifier resource) {
        boolean contains = super.contains(resourceType, resource);
        if(contains || resourceType.equals(ResourceType.SERVER_DATA))
            return contains;
        String path = String.format("programmer_art/%s/%s/%s", resourceType.getDirectory(), resource.getNamespace(), resource.getPath());
        try{
            Enumeration<URL> urls = DefaultResourcePack.class.getClassLoader().getResources(path);
            URL url = null;

            while(urls.hasMoreElements())
                url = urls.nextElement();

            return isValid(path, url);
        }catch(IOException e){
            return false;
        }
    }

    private static boolean isValid(String path, URL url) throws IOException {
        return url != null && (url.getProtocol().equals("jar") || DirectoryResourcePack.isValidPath(new File(url.getFile()), "/"+path));
    }

    @Override
    public InputStream open(ResourceType resourceType, Identifier resource) throws IOException {
        if(super.contains(resourceType, resource))
            return super.open(resourceType, resource);
        String path = String.format("programmer_art/%s/%s/%s", resourceType.getDirectory(), resource.getNamespace(), resource.getPath());
        try{
            Enumeration<URL> urls = DefaultResourcePack.class.getClassLoader().getResources(path);
            URL url = null;

            while(urls.hasMoreElements()){
                url = urls.nextElement();
            }

            if(isValid(path, url))
                return url.openStream();
        }catch(IOException e){

        }
        throw new FileNotFoundException(resource.getPath());
    }

    @Override
    public Set<String> getNamespaces(ResourceType resourceType) {
        Set<String> namespaces = super.getNamespaces(resourceType);

        if(!resourceType.equals(ResourceType.CLIENT_RESOURCES))
            return namespaces;

        for(ModContainer container : FabricLoader.getInstance().getAllMods()){
            Path path = container.getRootPath();
            path = path.toAbsolutePath().normalize();
            
            Path childPath = path.resolve(("programmer_art/"+resourceType.getDirectory()).replace("/", path.getFileSystem().getSeparator())).toAbsolutePath().normalize();
            if(childPath.startsWith(path) && Files.exists(childPath)){
                try(DirectoryStream<Path> dirs = Files.newDirectoryStream(childPath, Files::isDirectory)){
                    for(Path dir : dirs){
                        String name = dir.getFileName().toString();
                        name = name.replace(path.getFileSystem().getSeparator(), "");

                        if(RESOURCE_PACK_PATH.matcher(name).matches()){
                            namespaces.add(name);
                        }else{
                            ProgrammerArtInjector.LOGGER.warn("Ignoring invalid namespace {} in mod {}", name, container.getMetadata().getId());
                        }
                    }
                }catch(IOException e){

                }
            }
        }

        return namespaces;
    }

    @Override
    protected InputStream openFile(String name) throws IOException {
        if(name.equals("pack.png")){
            return DefaultResourcePack.class.getClassLoader().getResources("programmer_art/"+name).nextElement().openStream();
        }
        return super.openFile(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T parseMetadata(ResourceMetadataReader<T> metaReader) throws IOException {
        if(metaReader instanceof PackResourceMetadataReader)
            return super.parseMetadata((ResourceMetadataReader<T>) READER);
        return super.parseMetadata(metaReader);
    }

    private static final String DESCRIPTION = "The classic look of Minecraft,\n§8§onow with some mods!";
    private static final CustomMetadataReader READER = new CustomMetadataReader(DESCRIPTION);

    private static class CustomMetadataReader implements ResourceMetadataReader<PackResourceMetadata>{

        private final Text text;

        public CustomMetadataReader(String description) {
            this.text = new LiteralText(description);
        }
        
        public PackResourceMetadata fromJson(JsonObject jsonObject) {
            int pack_format = JsonHelper.getInt(jsonObject, "pack_format");
            return new PackResourceMetadata(text, pack_format);
        }

        public String getKey() {
            return "pack";
        }
    }

}