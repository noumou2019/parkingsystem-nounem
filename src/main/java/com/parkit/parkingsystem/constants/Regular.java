package com.parkit.parkingsystem.constants;

/**
 * Constants to manage user regularity.
 */
public final class Regular {
    private Regular() { }
    /**
     * Price reduction if regulated.
     */
    public static final Double REGULAR_REDUCTION = 0.95; // 5% de reduction
    /**
     * Duration of verification (in months).
     */
    public static final int MONTH_FOR_REDUCTION = 1; // Mois de vérification
    /**
     * Minimum number of uses to activate the reduction.
     */
    public static final int MINIMUM_REGULAR = 3; // Passage minimum
}
