    #type vertex
    #version 330 core
    // |----------VAO------------|
    // |                         |
    // |     Layout 0 (x,y,z)    |
    // |-------------------------|
    // |    Layout 1 (r,g,b,a)   |
    // |-------------------------|
    // |      Layout 2 (UV)      |
    // |-------------------------|

    layout (location=0) in vec3 aPos; //attribute position as input (in)
    layout (location=1) in vec4 aColor; //attribute color
//    layout (location=2) in vec2 aTexCoords; //attribute uv (Texture coord)

    //UNIFORM: ATTRIBUTI CHE SI APPLICANO A TUTTI I VAO NON SONO SPECIFICI DI CIASUNO
    uniform mat4 uProj; //projection matrix
    uniform mat4 uView; //view matrix


    out vec4 fColor; //output color for the fragment shader as output (out)
//    out vec2 fTexCoords; //coords of the textires passed to the fragment shader


    void main()
    {
        fColor = aColor;
//        fTexCoords = aTexCoords;
        gl_Position = uProj * uView * vec4(aPos, 1.0); //4 vector wity my 3 coord aPos and 1.0
    }
    //the position is passed to the fragment and it uses it interpolating between vertex
    #type fragment
    #version 330 core

//    uniform float uTime;
//    uniform sampler2D TEX_SAMPLER; //SAMPLE THE IMAGE

    in vec4 fColor; //accept the output of vertex shader as input
//    in vec2 fTexCoords; //accept the coords of the texture
    out vec4 color; //color we output

    float rand2D(in vec2 co){
        return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
    }
    float rand3D(in vec3 co){
        return fract(sin(dot(co.xyz ,vec3(12.9898,78.233,144.7272))) * 43758.5453);
    }

    float simple_interpolate(in float a, in float b, in float x)
    {
        return a + smoothstep(0.0,1.0,x) * (b-a);
    }
    float interpolatedNoise3D(in float x, in float y, in float z)
    {
        float integer_x = x - fract(x);
        float fractional_x = x - integer_x;

        float integer_y = y - fract(y);
        float fractional_y = y - integer_y;

        float integer_z = z - fract(z);
        float fractional_z = z - integer_z;

        float v1 = rand3D(vec3(integer_x, integer_y, integer_z));
        float v2 = rand3D(vec3(integer_x+1.0, integer_y, integer_z));
        float v3 = rand3D(vec3(integer_x, integer_y+1.0, integer_z));
        float v4 = rand3D(vec3(integer_x+1.0, integer_y +1.0, integer_z));

        float v5 = rand3D(vec3(integer_x, integer_y, integer_z+1.0));
        float v6 = rand3D(vec3(integer_x+1.0, integer_y, integer_z+1.0));
        float v7 = rand3D(vec3(integer_x, integer_y+1.0, integer_z+1.0));
        float v8 = rand3D(vec3(integer_x+1.0, integer_y +1.0, integer_z+1.0));

        float i1 = simple_interpolate(v1,v5, fractional_z);
        float i2 = simple_interpolate(v2,v6, fractional_z);
        float i3 = simple_interpolate(v3,v7, fractional_z);
        float i4 = simple_interpolate(v4,v8, fractional_z);

        float ii1 = simple_interpolate(i1,i2,fractional_x);
        float ii2 = simple_interpolate(i3,i4,fractional_x);

        return simple_interpolate(ii1 , ii2 , fractional_y);
    }



    float Noise3D(in vec3 coord, in float wavelength)
    {
        return interpolatedNoise3D(coord.x/wavelength, coord.y/wavelength, coord.z/wavelength);
    }
    void main()
    {
        //color = sin(uTime) * fColor; //just pass position and color (here color for the fragment shader)
        //HERE WE USE TIME TO CHANGE THE COLOR USIN SIN OF TIME OF EACH FRAME
        //GETTIN AVG = BIANCO E NERO
//        float avg = (fColor.r+fColor.g+fColor.b) / 3;
//        color = vec4(avg,avg,avg,1);
        //COMPUTING NOISE
//        float noise = fract(sin(dot(fColor.zy, vec2(12.9898,78.233)))*43758.5453);
//        color = fColor * noise;
        color = fColor;
//        color = texture(TEX_SAMPLER, fTexCoords);

    }