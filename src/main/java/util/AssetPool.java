package util;

import components.Spritesheet;
import renderer.Shader;
import renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {

    private static Map<String, Shader>  shaders = new HashMap<>();
    private static Map<String, Texture>  textures = new HashMap<>();
    private static Map<String, Spritesheet> spriteSheets = new HashMap<>();

    public static  Shader getShader(String resourceName){
        File file = new File(resourceName);
        if(AssetPool.shaders.containsKey(file.getAbsolutePath())){
            return shaders.get(file.getAbsolutePath());
        }else{
            Shader shader = new Shader(resourceName);
            shader.compileAndLink();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName){
        File file = new File(resourceName);
        if(AssetPool.textures.containsKey(file.getAbsolutePath())){
            return AssetPool.textures.get(file.getAbsolutePath());
        }else{
            Texture texture = new Texture(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpritesheet(String resourceName, Spritesheet spritesheet){
        File file = new File(resourceName);
        if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())){
            //Se ancora non c'è tra gli spritesheets
            AssetPool.spriteSheets.put(file.getAbsolutePath(),spritesheet);
        }
    }

    public static Spritesheet getSpritesheet(String resourceName) {
        File file = new File(resourceName);
        if (!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())) {
            //Se ancora non c'è tra gli spritesheets ERRORE!!!
            assert false : "Error: Tried to access spritesheet '"+resourceName+
                    "' and it has not been added to asset pool.";
            }
        return AssetPool.spriteSheets.getOrDefault(file.getAbsolutePath(),null); //null come default

    }

}
