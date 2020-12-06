#version 140

uniform sampler2D texture;
in vec2 uv;

uniform vec4 color;
uniform bool useTexture;

void main() {
    gl_FragColor = useTexture ? texture2D(texture, uv) : color;
}