package edu.kit.informatik.entity.board;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.entity.GamePiece;
import edu.kit.informatik.entity.IGamePieceProvider;
import edu.kit.informatik.entity.Player;
import edu.kit.informatik.exception.SemanticsException;
import edu.kit.informatik.exception.SyntaxException;

public final class Board {
    public static final int SIZE = 40;
    // also number of GamePieces
    public static final int DESTINATION_FIELD_NUMBER = 4;
    public static final String STD_FIELD = "SR,SR,SR,SR;SB,SB,SB,SB;SG,SG,SG,SG;SY,SY,SY,SY";
    private final BasicField[] mStandardFields = new BasicField[SIZE];
    private final Map<Player, DestinationField[]> mDestinationFields = new HashMap<>();
    private final Map<Player, ArrayDeque<GamePiece>> mStartGamePieces = new HashMap<>();
    private final IGamePieceProvider mGamePieceProvider;
    
    public Board(IGamePieceProvider pGamePieceProvider, Map<Player, FieldIdentifier[]> pFieldIdentifiers, int pMaxPiecesPerStdField, int pMaxPiecesPerDestField) throws SemanticsException {
        this.mGamePieceProvider = pGamePieceProvider;
        // populate standard fields
        IntStream.range(0, SIZE).forEach((x) -> mStandardFields[x] = new BasicField(x, pMaxPiecesPerStdField));
        
        // populate destination fields
        for(Player cPlayer : Player.values()) {
            DestinationField[] cFields = new DestinationField[DESTINATION_FIELD_NUMBER];
            for(int i = 0; i < DESTINATION_FIELD_NUMBER; i++) {
                cFields[i] = new DestinationField(SIZE + i, pMaxPiecesPerDestField, cPlayer);
            }
            this.mDestinationFields.put(cPlayer, cFields);
        }
        
        // populate start positions
        Stream.of(Player.values()).forEach(p -> this.mStartGamePieces.put(p, new ArrayDeque<>(4)));
        
        Set<Entry<Player, FieldIdentifier[]>> entrySet = pFieldIdentifiers.entrySet();
        Iterator<Entry<Player, FieldIdentifier[]>> it = entrySet.iterator();
        while(it.hasNext()) {
            Entry<Player, FieldIdentifier[]> entry = it.next();
            FieldIdentifier[] cIdentifiers = entry.getValue();
            Player cPlayer = entry.getKey();
            for(FieldIdentifier cIdentifier : cIdentifiers) {
                if(cIdentifier.getIndex() != FieldIdentifier.START_POSITION_INDEX) {
                    BasicField cField = getField(cIdentifier);
                    cField.putGamePiece(this.mGamePieceProvider.createGamePiece(cPlayer, cField));
                } else {
                    Player cIdentifierPlayer = cIdentifier.extractPlayer();
                    if(cIdentifierPlayer != cPlayer) {
                        throw new SemanticsException("Error, cannot place a piece of player " + cIdentifierPlayer + " on start position of player " + cPlayer + ".");
                    }
                    // TODO check if it would not be better to replace null with BasicField.getStartField(cPlayer)!
                    addStartGamePiece(cPlayer, this.mGamePieceProvider.createGamePiece(cPlayer, null));
                }
            }
        }
    }
    
    private void addStartGamePiece(Player cPlayer, GamePiece pGamePiece) {
        this.mStartGamePieces.get(cPlayer).addLast(pGamePiece);
    }
    
    private void removeStartGamePiece(Player cPlayer, GamePiece pGamePiece) {
        if(this.mStartGamePieces.size() == 0) {
            throw new IllegalStateException("Error, you cannot take a piece from start field. There are no pieces on start field anymore.");
        }
        
        if(!this.mStartGamePieces.get(cPlayer).remove(pGamePiece)) {
            throw new IllegalStateException("Error, failed to remove game piece from start for player: " + cPlayer + ".");
        }
    }

    // external - sanitized
    private BasicField getField(String pIdentifierString) throws SyntaxException {
        return getField(new FieldIdentifier(pIdentifierString));
    }
    
    // internal - unsanitized
    private BasicField getField(FieldIdentifier pIdentifier) {
        return getField(pIdentifier.extractPlayer(), pIdentifier.getIndex());
    }

    // caution: throws exception if not found - but this should not happen with internal calls and external calls are sanitized via FieldIdentifier
    public BasicField getField(Player pPlayer, int pIndex) {
        if(pIndex < 0) {
            // return a wrapper
            return BasicField.getStartField(pPlayer);
            
            // the following is really bad (runtime exceptions might occur if programmers do not know this constraint or it is not propagated through calling methods)
            // throw new RuntimeException("Error, you must not query a start field.");
        } else if(pIndex < SIZE) {
            return this.mStandardFields[pIndex];
        } else if(pIndex < SIZE + DESTINATION_FIELD_NUMBER) {
            return this.mDestinationFields.get(pPlayer)[pIndex - SIZE];
        } else {
            throw new IllegalArgumentException("Error, illegal field index: " + pIndex + "; too large");
        }
    }
    
    // caution: throws exception if not found - but this should not happen with internal calls and external calls are sanitized via FieldIdentifier
    public BasicField getFieldRelative(Player pPlayer, int pRelativeIndex) {
        if(pRelativeIndex < 0) {
            // return a wrapper
            return BasicField.getStartField(pPlayer);
            
            // the following is really bad (runtime exceptions might occur if programmers do not know this constraint or it is not propagated through calling methods)
            //throw new RuntimeException("Error, you must not query a start field.");
        } else if(pRelativeIndex < SIZE) {
            return getField(pPlayer, getAbsoluteFromRelativePosition(pPlayer, pRelativeIndex));
        } else if(pRelativeIndex < SIZE + DESTINATION_FIELD_NUMBER) {
            return this.mDestinationFields.get(pPlayer)[pRelativeIndex - SIZE];
        } else {
            throw new IllegalArgumentException("Error, illegal field index: " + pRelativeIndex + "; too large");
        }
    }
    

    // TODO test exclusive arg
    public List<BasicField> getFieldsRelative(Player pPlayer, int pFrom, int pTo, boolean isForwardMove, boolean isExclusive) {
        if(isExclusive) {
            if(isForwardMove) {
                pFrom = (pFrom == Board.SIZE + DESTINATION_FIELD_NUMBER - 1) ? 0 : pFrom + 1;
            } else {
               pFrom = (pFrom == 0) ? Board.SIZE + DESTINATION_FIELD_NUMBER - 1 : pFrom - 1;  
            }
        }
        
        List<BasicField> fields = new ArrayList<>();
        if(pFrom <= pTo) {
            if(isForwardMove) {
                for(int i = pFrom; i <= pTo; i++) {
                    fields.add(getFieldRelative(pPlayer, i));
                }
            } else {
                for(int i = pFrom; i >= 0; i--) {
                    fields.add(getFieldRelative(pPlayer, i));
                }
                
                for(int i = Board.SIZE - 1; i >= pTo; i--) {
                    fields.add(getFieldRelative(pPlayer, i));
                }
            }
        } else {
            if(isForwardMove) {
                for(int i = pFrom; i < Board.SIZE; i++) {
                    fields.add(getFieldRelative(pPlayer, i));
                }   
                
                for(int i = 0; i <= pTo; i++) {
                    fields.add(getFieldRelative(pPlayer, i));
                }
            } else {
                for(int i = pFrom; i >= pTo; i--) {
                    fields.add(getFieldRelative(pPlayer, i));
                }
            }
        }
        
        return fields;
    }
    
    @Deprecated
    public List<BasicField> getFields(Player pPlayer, int pFrom, int pTo, boolean isForwardMove) {
        return getFields(pPlayer, pFrom, pTo, isForwardMove, true);
    }
    
    // note: not designed for indices between DestinationFields and 
    // from exclusive, to inclusive!!
    @Deprecated
    public List<BasicField> getFields(Player pPlayer, int pFrom, int pTo, boolean isForwardMove, boolean isExclusive) {
        if(isExclusive) {
            if(isForwardMove) {
                pFrom = (pFrom == Board.SIZE - 1) ? 0 : pFrom + 1;
            } else {
               pFrom = (pFrom == 0) ? Board.SIZE - 1 : pFrom - 1;  
            }
        }
        
        List<BasicField> fields = new ArrayList<>();
        if(pFrom <= pTo) {
            if(isForwardMove) {
                for(int i = pFrom; i <= pTo; i++) {
                    fields.add(getField(pPlayer, i));
                }
            } else {
                for(int i = pFrom; i >= 0; i--) {
                    fields.add(getField(pPlayer, i));
                }
                
                for(int i = Board.SIZE - 1; i >= pTo; i--) {
                    fields.add(getField(pPlayer, i));
                }
            }
        } else {
            if(isForwardMove) {
                for(int i = pFrom; i < Board.SIZE; i++) {
                    fields.add(getField(pPlayer, i));
                }   
                
                for(int i = 0; i <= pTo; i++) {
                    fields.add(getField(pPlayer, i));
                }
            } else {
                for(int i = pFrom; i >= pTo; i--) {
                    fields.add(getField(pPlayer, i));
                }
            }
        }
        
        return fields;
    }
    
    public static boolean isValidFieldIndex(int pIndex) {
        return pIndex >= 0 && pIndex < SIZE + DESTINATION_FIELD_NUMBER;
    }
    
    // return null if not available
    // get AND remove!!
    private GamePiece getNextGamePiece(Player pPlayer) {
        return this.mStartGamePieces.get(pPlayer).pollFirst();
    }
    
    public int getNumberOfPiecesOnStart(Player pPlayer) {
        return this.mStartGamePieces.get(pPlayer).size();
    }
    
    public void beatAll(BasicField pField, GamePiece pBeatingGamePiece) throws SemanticsException {
        if(!pField.canBeat(pBeatingGamePiece)) {
            throw new SemanticsException("Error, cannot beat anything on given field with index: " + pField.getAbsolutePosition() + ".");
        } else {
            Set<GamePiece> beatenPieces = pField.removeAllPieces();
            
            /* place the beaten ones back on start position */
            Iterator<GamePiece> it = beatenPieces.iterator();
            while(it.hasNext()) {
                GamePiece cGamePiece = it.next();
                cGamePiece.setCurrentField(null);
                addStartGamePiece(cGamePiece.getOwner(), cGamePiece);
            }
        }
    }

    public void placeGamePiece(BasicField pField, GamePiece pGamePiece) throws SemanticsException {
        /* free old position */
        if(!pGamePiece.isOnStartField()) {
            pGamePiece.getCurrentField().removePiece(pGamePiece);
        } else {
            removeStartGamePiece(pGamePiece.getOwner(), pGamePiece);
        }
        
        /* assign new position */
        pField.putGamePiece(pGamePiece);
        pGamePiece.setCurrentField(pField);
    }
    
    private static String S = "\t";
    private static String NL = "\n";
    
    private String sx(Player pPlayer, int num) {
        return (getNumberOfPiecesOnStart(pPlayer) >= num) ? getStartFieldName(pPlayer) : "";
    }
    
    // inclusive!!
    private String ft(int from, int to) {
        String out = "";
        if(from <= to) {
            for(int i = from; i <= to; i++) {
                out += ((i > from) ? S : "") + getField(null, i).toStringX();
            }
        } else {
            for(int i = from; i >= to; i--) {
                out += ((i < from) ? S : "") + getField(null, i).toStringX();
            }   
        }
        
        return out;
    }
    
    private String ft(int index) {
        return ft(index, index);
    }
    
    private String ft(String id) {
        try {
            BasicField cField = getField(id);
            return cField.toStringX();
        } catch (SyntaxException e) {
            throw new RuntimeException(e);  // will not happen
        }
    }
    
    public void printx() {        
        String out =  sx(Player.RED, 1) + S + sx(Player.RED, 2) + S + S + S + ft(8, 10) + S + S + S + sx(Player.BLUE, 1) + S + sx(Player.BLUE, 2) + NL;
               out += sx(Player.RED, 3) + S + sx(Player.RED, 4) + S + S + S + ft(7) + S + ft("AB") + S + ft(11) + S + S + S + sx(Player.BLUE, 3) + S + sx(Player.BLUE, 4) + NL;
               out += S + S + S + S + ft(6) + S + ft("BB") + S + ft(12) + S + S + S + S + NL;
               out += S + S + S + S + ft(5) + S + ft("CB") + S + ft(13) + S + S + S + S + NL;
               out += ft(0, 4) + S + ft("DB") + S + ft(14, 18) + NL;
               out += ft(39) + S + ft("AR") + S + ft("BR") + S + ft("CR") + S + ft("DR") + S + S + ft("DG") + S + ft("CG") + S + ft("BG") + S + ft("AG") + S + ft(19) + NL;
               out += ft(38, 34) + S + ft("DY") + S + ft(24, 20) + NL;
               out += S + S + S + S + ft(33) + S + ft("CY") + S + ft(25) + S + S + S + S + NL;
               out += S + S + S + S + ft(32) + S + ft("BY") + S + ft(26) + S + S + S + S + NL;
               out += sx(Player.YELLOW, 1) + S + sx(Player.YELLOW, 2) + S + S + S + ft(31) + S + ft("AY") + S + ft(27) + S + S + S + sx(Player.GREEN, 1) + S + sx(Player.GREEN, 2) + NL;
               out += sx(Player.YELLOW, 3) + S + sx(Player.YELLOW, 4) + S + S + S + ft(30, 28) + S + S + S + sx(Player.GREEN, 3) + S + sx(Player.GREEN, 4);
               
         Terminal.printLine(out);
    }

    public static String getStartFieldName(Player pPlayer) {
        return "S" + pPlayer.getId();
    }
    
    public static int getAbsoluteFromRelativePosition(Player pOtherPlayer, int pRelativePosition) {
        if(pRelativePosition == FieldIdentifier.START_POSITION_INDEX) {
            return FieldIdentifier.START_POSITION_INDEX;
        } else if(pRelativePosition >= 0 && pRelativePosition < SIZE) {
            int absolutePosition = pRelativePosition + pOtherPlayer.getStartPosition();
            
            if(absolutePosition < SIZE) {
                return absolutePosition;
            } else {
                return absolutePosition - SIZE;
            }
        } else if(pRelativePosition >= 0 && pRelativePosition < SIZE + DESTINATION_FIELD_NUMBER) {
            return pRelativePosition;
        } else {
            throw new IllegalArgumentException("Error, relative index invalid: " + pRelativePosition + ".");
        }
    }
}
