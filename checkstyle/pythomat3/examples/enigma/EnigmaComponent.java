package enigma;

/**
 * Interface EnigmaComponent unifies rotors, patchboard and reflector
 * of an enigma.
 */
public interface EnigmaComponent {

    /**
     * Applies the substitution chiffre of this enigma component in the current
     * state to letter
     * @param letter The letter to encode with the substitution chiffre of this
     *               enigma component in its current state
     * @return The letter which is the encoded letter of the parameter letter
     */
    int encode(int letter);

    /**
     * Applies the inverse substitution chiffre of this enigma component in the
     * current state to letter
     * @param letter The letter to decode with the (inverse) substitution
     *               chiffre of this enigma component in its current state
     * @return The letter which is the decoded letter of the parameter letter
     */
    int decode(int letter);

    /**
     * In case this component can do ticks, this component performs its next
     * tick. In case this triggers ticks of subsequent components, their ticks
     * are also triggered.
     */
    void tick();
}
