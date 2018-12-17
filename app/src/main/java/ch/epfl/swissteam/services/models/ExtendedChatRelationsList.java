package ch.epfl.swissteam.services.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The purpose of the ExtendedChatRelationList is to contain completed ExtendedChatRelation (see class
 * description of ExtendedChatRelation for more info). This class implements Observable since it is
 * also designed to filter its content based on a keyword which means that changes can occur.
 * @author Sébastien Gachoud
 */
public class ExtendedChatRelationsList extends ArrayList<ExtendedChatRelation> implements Observable {
    List<Observer> observers_ = new ArrayList<>();
    String filterName_;

    /**
     *
     * @param chatRelations A list of chatRelation that should be extended
     * @param currentUserId The current logged user.
     */
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

    /**
     * set the filter keyword that will be use to filter the list by name of the chat partner
     * @param filterName_ part or full name of the partner who is seek
     */
    public void setFilterName_(String filterName_) {
        this.filterName_ = filterName_;
        notifyObservers();
    }

    /**
     *
     * @return  the filtered list of relation based on the filterName
     */
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
