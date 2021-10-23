#version 140

uniform sampler2D texture;
in vec2 uv;

uniform vec4 color;
uniform bool useTexture;
uniform vec2 iResolution;

void main() {
    float Pi = 6.28318530718; // Pi*2

    // GAUSSIAN BLUR SETTINGS {{{
    float Directions = 128.0; // BLUR DIRECTIONS (Default 16.0 - More is better but slower)
    float Quality = 128.0; // BLUR QUALITY (Default 4.0 - More is better but slower)
    float Size = 16.0; // BLUR SIZE (Radius)
    // GAUSSIAN BLUR SETTINGS }}}

    vec2 Radius = Size/iResolution.xy;

    // Normalized pixel coordinates (from 0 to 1)
    vec2 uv2 = gl_FragCoord.xy/iResolution.xy;
    // Pixel colour
    vec3 Color = texture2D(texture, uv2).rgb;

    // Blur calculations
    for(float d=0.0; d<Pi; d+=Pi/Directions)
    {
		for(float i=1.0/Quality; i<=1.0; i+=1.0/Quality)
        {
			Color += texture2D(texture, uv2+vec2(cos(d),sin(d))*Radius*i).rgb;
        }
    }

    // Output to screen
    Color /= Quality * Directions;
    gl_FragColor = color * vec4(Color,1);
}