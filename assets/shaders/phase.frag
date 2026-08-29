#define HIGHP

uniform sampler2D u_texture;
uniform float u_time;
uniform float u_alpha;
uniform vec2 u_uv;
uniform vec2 u_uv2;
uniform vec2 u_texsize;

varying vec2 v_texCoords;

void main() {
	vec2 T = v_texCoords.xy;
	vec2 coords = (v_texCoords - u_uv) / (u_uv2 - u_uv) * u_texsize;

	T += vec2(sin(coords.y / 3.0 + u_time / 20.0), sin(coords.x / 3.0 + u_time / 20.0)) / u_texsize;

	vec4 color = texture2D(u_texture, T);

	if (color.a > 0.0) {
		if (mod(coords.x + coords.y + sin(coords.x / 5.0) * 3.0 + sin(coords.y / 5.0) * 3.0  + u_time / 4.0, 10.0) < 2.0) {
			color *= 1.65;
		}
		color.a = u_alpha;
	}

	gl_FragColor = color;
}