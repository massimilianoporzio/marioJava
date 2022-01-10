    #type vertex
    #version 330 core
    layout (location=0) in vec3 aPos; //attribute position
    layout (location=1) in vec4 aColor; //attribute color

    uniform mat4 uProj; //projection matrix
    uniform mat4 uView; //view matrix

    out vec4 fColor; //output color for the fragment shader

    void main()
    {
        fColor = aColor;
        gl_Position = uProj * uView * vec4(aPos, 1.0); //4 vector wity my 3 coord aPos and 1.0
    }

    #type fragment
    #version 330 core

    in vec4 fColor; //accept the output of vertex shader as input
    out vec4 color; //color we output

    void main()
    {
        color = fColor; //just pass position and color (here color for the fragment shader)
    }