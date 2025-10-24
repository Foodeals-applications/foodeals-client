package net.foodeals.dlc.domain.enums;

public enum ValorisationType {
    URGENTE("urgente"),
    EXIGEE("exigée"),
    SOUHAITABLE("souhaitable");

    private final String label;

    ValorisationType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static ValorisationType getValorisationType(int daysRemaining) {
        if (daysRemaining <= 10) {
            return URGENTE;
        } else if (daysRemaining <= 20) {
            return EXIGEE;
        } else {
            return SOUHAITABLE;
        }
    }
}

