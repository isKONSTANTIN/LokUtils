#version 140
#extension GL_ARB_explicit_attrib_location : enable

uniform mat4 projection_matrix;
uniform mat4 view_matrix;
layout(location = 0) in vec2 vertex;

void main() {
    gl_Position = projection_matrix * view_matrix * vec4(vertex, 0, 1.0);
}