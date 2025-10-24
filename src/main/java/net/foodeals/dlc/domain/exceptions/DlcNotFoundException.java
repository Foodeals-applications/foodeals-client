package net.foodeals.dlc.domain.exceptions;

import java.util.UUID;

public class DlcNotFoundException extends RuntimeException {
    private  UUID id;
    

    public DlcNotFoundException(UUID id) {
        super(" dlcwith id " + id + " not found");
        this.id = id;
    }
    
   
}