package com.backend.freelance.models;

public enum JobApplicationsStatus {
    PENDING,
    ACCEPTED,
    REJECTED;

    public static final JobApplicationsStatus[] APPLICATIONS_STATUSES = new JobApplicationsStatus[] {
        PENDING,
        ACCEPTED,
        REJECTED
    };
}
