package com.extracraftx.minecraft.programmerartinjector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.ZipResourcePack;
import net.minecraft.util.Identifier;

public class ProgrammerArtResourcePack extends ZipResourcePack {

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
        String path = String.format("programmer_art/%s/%s/%s", resourceType.getName(), resource.getNamespace(), resource.getPath());
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
        String path = String.format("programmer_art/%s/%s/%s", resourceType.getName(), resource.getNamespace(), resource.getPath());
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

}