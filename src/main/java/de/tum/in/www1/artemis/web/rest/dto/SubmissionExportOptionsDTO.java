package de.tum.in.www1.artemis.web.rest.dto;

import java.time.ZonedDateTime;

/**
 * This is a dto for the repository export options.
 */
public class SubmissionExportOptionsDTO {

    private boolean exportAllParticipants;

    private boolean filterLateSubmissions;

    private ZonedDateTime filterLateSubmissionsDate;

    private String participantIdentifierList;

    public boolean isExportAllParticipants() {
        return exportAllParticipants;
    }

    public void setExportAllParticipants(boolean exportAllParticipants) {
        this.exportAllParticipants = exportAllParticipants;
    }

    public boolean isFilterLateSubmissions() {
        return filterLateSubmissions;
    }

    public void setFilterLateSubmissions(boolean filterLateSubmissions) {
        this.filterLateSubmissions = filterLateSubmissions;
    }

    public ZonedDateTime getFilterLateSubmissionsDate() {
        return filterLateSubmissionsDate;
    }

    public void setFilterLateSubmissionsDate(ZonedDateTime filterLateSubmissionsDate) {
        this.filterLateSubmissionsDate = filterLateSubmissionsDate;
    }

    public String getParticipantIdentifierList() {
        return participantIdentifierList;
    }

    public void setParticipantIdentifierList(String participantIdentifierList) {
        this.participantIdentifierList = participantIdentifierList;
    }
}
