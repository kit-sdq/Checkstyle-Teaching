package student;

import enigma.EnigmaComponent;

public class Enigma {

    private EnigmaComponent[] components;

    public Enigma(int[] patchboard, int rotorCount, int[] reflector) {
	assert(rotorCount >= 0);
	components = new EnigmaComponent[rotorCount + 2];
	components[0] = new Patchboard(patchboard);
	components[components.length - 1] = new Reflector(reflector);
    }

    public boolean addRotor(int[] permutation, int tickPos, int position) {
	int i = components.length - 2;
	while (i > 0 && components[i] != null) {
	    i--;
	}
	if (i == 0) {
	    return false;
	} else {
	    components[i] = new EnigmaRotor(permutation, tickPos, position,
					    components[i + 1]);
	    return true;
	}
    }

    public int encode(int letter) {
	int temp = letter;

	for (int i = 0; i < components.length; i++) {
	    temp = components[i].encode(temp);
	}

	for (int i = components.length - 2; i >= 0; i--) {
	    temp = components[i].decode(temp);
	}

	components[1].tick();

	return temp;
    }
}
