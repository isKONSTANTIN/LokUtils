#version 140
#extension GL_ARB_explicit_attrib_location : enable

uniform mat4 projection_matrix;
uniform mat4 view_matrix;

in vec2 vertex;
in vec2 frag_uv;

out vec2 uv;

void main() {
    gl_Position = projection_matrix * view_matrix * vec4(vertex, 0, 1);

    uv = frag_uv;
}