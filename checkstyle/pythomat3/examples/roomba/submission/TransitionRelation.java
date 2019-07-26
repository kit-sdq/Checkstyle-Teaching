package student;

import java.util.HashMap;

import student.program.*;

public class TransitionRelation {
    public class Key {
        Programs program;
        Program.ReturnState state;
      
        int num_programs = Programs.length;
        
        public Key(Programs program, Program.ReturnState state) {
            this.program = program;
            this.state = state;
        }
        
        @Override
        public int hashCode() {
            return state.ordinal() * num_programs + program.ordinal();
        }
        
        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof Key)) {
                return false;
            }
            Key k = (Key)o;
            return k.program == this.program && k.state == this.state;
        }
    }
    
    HashMap<Key, Programs> relation = new HashMap<Key, Programs>();
    
    public void add(Key key, Programs program) {
        relation.put(key, program);
    }
    
    public Programs get(Key key) {
        return relation.get(key);
    }
}
