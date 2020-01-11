# <img src="https://imgur.com/Hu7FfAg.png" align="right" width=64 />ProgrammerArtInjector
This is a library mod that allows other mods to inject resources into the "Programmer Art" resource pack.

Now you can make your textures fit in, even when people are using the classic textures!

## Maven
```gradle
repositories {
    maven { url "https://maven.extracraftx.com"}
}
dependencies {
    modImplementation "com.extracraftx.minecraft:ProgrammerArtInjector:{VERSION}"
    include "com.extracraftx.minecraft:ProgrammerArtInjector:{VERSION}"
}
```
`{VERSION}` should be replaced with the version of this mod you would like to bundle.

The `include` declaration makes it so that this mod is bundled with your mod. This means users will not have to download the mod separately.

### For older versions
If you would like to include this in a project that uses a Loom version < `0.2.6`, you will need to change the `modImplementation` line to the following:
```gradle
modImplementation ("com.extracraftx.minecraft:ProgrammerArtInjector:{VERSION}"){
    exclude group: 'net.fabricmc'
}
```

## Usage
To include textures for programmer art, add a folder named `programmer_art` to your `resources` folder. In here, you can follow the normal `resources` folder structure.

Example:  
`resources/programmer_art/assets/modid/textures/block/some_block.png`

This mod will inject these resources into the "Programmer Art" resource pack.