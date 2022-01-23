#type vertex
#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;
layout (location=4) in float aEntityId; //id dell'oggetto (GameObjectID)


uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;
out float fEntityId;

void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;
    fEntityId = aEntityId;

    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

    #type fragment
    #version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;
in float fEntityId;

uniform sampler2D uTextures[8];

out vec3 color; //NON USO IN OUTPUT IL BLENDING

void main()
{
    vec4 texColor = vec4(1,1,1,1);
    // in pratica dove c'è la texture (fTexId > 0) DISEGNA
    if (fTexId > 0) {
        int id = int(fTexId);
        texColor = fColor * texture(uTextures[id], fTexCoords);
        //color = vec4(fTexCoords, 0, 1);
    }
    //MA SE POI TROVO NELLA TEXTURE QUALCOSA CON ALPHA < 0.5
    if(texColor.a < 0.5){
        discard; // RIMUOVO PIXEL DA
    }
    //QUI FACCIO OVERRIDE E IN USCITA "COLORO" CON L'ID
    color = vec3(fEntityId,fEntityId,fEntityId);
}