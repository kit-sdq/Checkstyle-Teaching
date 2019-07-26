package edu.kit.informatik.entity.strategy;

import java.util.Collection;
import java.util.List;

import edu.kit.informatik.entity.GamePiece;
import edu.kit.informatik.entity.board.Board;
import edu.kit.informatik.entity.movement.Movement;

//TODO make generics more extendible via wildcard
public final class FilterChain<S> {       
    private Filter<S> mCurrentFilter;
    
    public FilterChain(final S pDefaultValue) {
        this.mCurrentFilter = new Filter<S>() {

            @Override
            public S doFilter(Movement movement, List<GamePiece> pGamePieces, Board pBoard, Collection<Filter<S>> pExcludedFilter) {
                return pDefaultValue;
            }
        };
    }
    
    public final S doFilter(Movement movement, List<GamePiece> pGamePieces, Board pBoard, Collection<Filter<S>> pExcludedFilter) {
        if(Filter.isExcluded(this.mCurrentFilter, pExcludedFilter)) {
            return this.mCurrentFilter.next(movement, pGamePieces, pBoard, pExcludedFilter);
        } else {
            return this.mCurrentFilter.doFilter(movement, pGamePieces, pBoard, pExcludedFilter);
        }
    }
    
    public final void addFront(Filter<S> pFilter) {
        pFilter.setNext(this.mCurrentFilter);
        this.mCurrentFilter = pFilter;
    }
    
    public static abstract class Filter<T> {
        private Filter<T> mNextFilter;
        
        public Filter() { }

        public final void setNext(Filter<T> pNextFilter) {
            this.mNextFilter = pNextFilter;
        }
        
        protected abstract T doFilter(Movement movement, List<GamePiece> pGamePieces, Board pBoard, Collection<Filter<T>> pExcludedFilter);

        public final T next(Movement movement, List<GamePiece> pGamePieces, Board pBoard, Collection<Filter<T>> pExcludedFilter) {
            if(Filter.isExcluded(this.mNextFilter, pExcludedFilter)) {
                return this.mNextFilter.next(movement, pGamePieces, pBoard, pExcludedFilter);
            } else {
                return this.mNextFilter.doFilter(movement, pGamePieces, pBoard, pExcludedFilter);
            }
        }        
        
        public static final <R> boolean isExcluded(Filter<R> pCurrentFilter, Collection<Filter<R>> pExcludedFilter) {
            for(Filter<R> cFilter : pExcludedFilter) {
                if(cFilter == pCurrentFilter) {
                    return true;
                }
            }
            
            return false;
        }
    }
}
