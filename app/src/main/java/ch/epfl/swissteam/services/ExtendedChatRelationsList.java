package ch.epfl.swissteam.services;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExtendedChatRelationsList extends ArrayList<ExtendedChatRelation> implements Observable{
    List<Observer> observers_ = new ArrayList<>();
    String filterName_;

    public ExtendedChatRelationsList(List<ChatRelation> chatRelations, String currentUserId){
        for(ChatRelation chatRelation : chatRelations){
            new ExtendedChatRelation(chatRelation, currentUserId, eCR ->{
                add(eCR);
                sort();
                notifyObservers();
            });
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers_.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers_.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer: observers_) {
            observer.update(this);
        }
    }

    public void setFilterName_(String filterName_) {
        this.filterName_ = filterName_;
        notifyObservers();
    }

    public List<ExtendedChatRelation> getFilteredRelations(){
        ArrayList<ExtendedChatRelation> filteredRelations = new ArrayList<>();
        for(ExtendedChatRelation relation : this){
            if(filterName_ == null || relation.getOthersName_().toLowerCase().contains(filterName_.toLowerCase())) {
                filteredRelations.add(relation);
            }
        }
        return filteredRelations;
    }

    private void sort(){
        Collections.sort(this, (eCR1, eCR2) -> {
            long diff = eCR2.getTimestamp_() - eCR1.getTimestamp_();
            if(diff < 0) return -1;
            else if(diff > 0) return 1;
            else return 0;
        });
    }
}
