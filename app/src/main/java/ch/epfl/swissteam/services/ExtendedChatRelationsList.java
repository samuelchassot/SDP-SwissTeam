package ch.epfl.swissteam.services;

import java.util.ArrayList;
import java.util.List;

public class ExtendedChatRelationsList extends ArrayList<ExtendedChatRelation> implements Observable{
    List<Observer> observers = new ArrayList<>();
    String filterName_;

    public ExtendedChatRelationsList(List<ChatRelation> chatRelations, String currentUserId){
        for(ChatRelation chatRelation : chatRelations){
            new ExtendedChatRelation(chatRelation, currentUserId, eCR ->{
                add(eCR);
                notifyObservers();
            });
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer: observers) {
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
}
