package student;

import enigma.EnigmaComponent;

public class EnigmaRotor implements EnigmaComponent {

    private int[] permutation;
    private int[] inversePermutation;
    private int tickPosition;
    private int position;

    private EnigmaComponent nextComponent;

    public EnigmaRotor(int[] permutation, int tickPosition, int position, 
		       EnigmaComponent nextComponent) {
	this.permutation = permutation;
	this.inversePermutation = new int[permutation.length];
	for (int i = 0; i < permutation.length; i++) {
	    inversePermutation[permutation[i]] = i;
	}
	this.tickPosition = tickPosition;
	this.position = position;
	this.nextComponent = nextComponent;
    }

    public int encode(int letter) {
	assert (letter >= 0 && letter < this.permutation.length);
	return (permutation[(letter + position) % permutation.length]
		- position + permutation.length) % permutation.length;
    }

    public int decode(int letter) {
	assert (letter >= 0 && letter < this.permutation.length);
	return (inversePermutation[(letter + position) % permutation.length]
		- position + permutation.length) % permutation.length;
    }

    public void tick() {
	if (this.position == this.tickPosition) {
	    nextComponent.tick();
	}
	this.position = (this.position + 1) % this.permutation.length;
    }
}
