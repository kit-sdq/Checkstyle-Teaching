package student;

import enigma.EnigmaComponent;

class Reflector implements EnigmaComponent {

    private int[] permutation;

    public Reflector(int[] permutation) {
	this.permutation = permutation;
    }

    public int encode(int letter) {
	assert (letter >= 0 && letter < this.permutation.length);
	return permutation[letter];
    }

    public int decode(int letter) {
	throw new UnsupportedOperationException("Decode not supported "
						+ "for Reflector class");
    }

    public void tick() {
    }
}
