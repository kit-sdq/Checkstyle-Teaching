package student;

import enigma.EnigmaComponent;

class Patchboard implements EnigmaComponent {

    private int[] permutation;
    private int[] inversePermutation;

    public Patchboard(int[] permutation) {
	this.permutation = permutation;
	this.inversePermutation = new int[permutation.length];
	for (int i = 0; i < permutation.length; i++) {
	    inversePermutation[permutation[i]] = i;
	}
    }

    public int encode(int letter) {
	assert (letter >= 0 && letter < this.permutation.length);
	return permutation[letter];
    }

    public int decode(int letter) {
	assert (letter >= 0 && letter < this.permutation.length);
	return inversePermutation[letter];
    }

    public void tick() {
	throw new UnsupportedOperationException("tick not supported"
						+ " in Patchboard");
    }
}
