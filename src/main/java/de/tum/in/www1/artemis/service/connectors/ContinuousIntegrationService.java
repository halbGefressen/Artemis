package de.tum.in.www1.artemis.service.connectors;

import java.net.URL;
import java.util.List;

import org.apache.http.HttpException;
import org.springframework.http.ResponseEntity;

import de.tum.in.www1.artemis.domain.*;

/**
 * Abstract service for managing entities related to continuous integration.
 */
public interface ContinuousIntegrationService {

    public enum BuildStatus {
        INACTIVE, QUEUED, BUILDING
    }

    /**
     * Creates the base build plan for the given programming exercise
     * 
     * @param exercise           a programming exercise with the required information to create the base build plan
     * @param planKey            the key of the plan
     * @param repositoryName     the slug of the assignment repository (used to separate between exercise and solution), i.e. the unique identifier
     * @param testRepositoryName the slug of the test repository, i.e. the unique identifier
     */
    void createBuildPlanForExercise(ProgrammingExercise exercise, String planKey, String repositoryName, String testRepositoryName);

    /**
     * Copy the base build plan for the given user on the CI system.
     *
     * @param templateBuildPlanId unique identifier for build plan on CI system
     * @param username            username of user for whom to copy build plan
     * @return unique identifier of the copied build plan
     */
    public String copyBuildPlan(String templateBuildPlanId, String username);

    /**
     * Configure the build plan with the given participation on the CI system. Common configurations: - update the repository in the build plan - set appropriate user permissions -
     * initialize/enable the build plan so that it works
     *
     * @param participation contains the unique identifier for build plan on CI system and the url of user's personal repository copy
     */
    public void configureBuildPlan(ProgrammingExerciseParticipation participation);

    /**
     * triggers a build for the build plan in the given participation
     * 
     * @param participation the participation with the id of the build plan that should be triggered
     * @throws HttpException if the request to the CI failed.
     */
    public void triggerBuild(ProgrammingExerciseParticipation participation) throws HttpException;

    /**
     * Delete project with given identifier from CI system.
     *
     * @param projectKey unique identifier for the project on CI system
     */
    public void deleteProject(String projectKey);

    /**
     * Delete build plan with given identifier from CI system.
     *
     * @param buildPlanId unique identifier for build plan on CI system
     */
    public void deleteBuildPlan(String buildPlanId);

    /**
     * Will be called when a POST request is sent to the '/results/{buildPlanId}'. Configure this as a build step in the build plan.
     * <p>
     * Important: The implementation is responsible for retrieving and saving the result from the CI system.
     * @param participation for which build has completed
     * @return build result
     */
    @Deprecated
    public Result onBuildCompletedOld(ProgrammingExerciseParticipation participation);

    /**
     * Get the plan key of the finished build, the information of the build gets passed via the requestBody. The requestBody must match the information passed from the
     * bamboo-server-notification-plugin, the body is described here: https://github.com/ls1intum/bamboo-server-notification-plugin
     *
     * @param requestBody The request Body received from the CI-Server.
     * @return the plan key of the build
     * @throws Exception if the Body could not be parsed
     */
    public String getPlanKey(Object requestBody) throws Exception;

    /**
     * Get the result of the finished build, the information of the build gets passed via the requestBody. The requestBody must match the information passed from the
     * bamboo-server-notification-plugin, the body is described here: https://github.com/ls1intum/bamboo-server-notification-plugin
     *
     * @param participation The participation for which the build finished
     * @param requestBody   The request Body received from the CI-Server.
     * @return the result of the build
     * @throws Exception if the Body could not be parsed
     */
    public Result onBuildCompletedNew(ProgrammingExerciseParticipation participation, Object requestBody) throws Exception;

    /**
     * Get the current status of the build for the given participation, i.e. INACTIVE, QUEUED, or BUILDING.
     *
     * @param participation participation for which to get status
     * @return build status
     */
    public BuildStatus getBuildStatus(ProgrammingExerciseParticipation participation);

    /**
     * Check if the given build plan ID is valid and accessible.
     *
     * @param buildPlanId unique identifier for build plan on CI system
     * @return true if build plan is valid otherwise false
     */
    public Boolean buildPlanIdIsValid(String buildPlanId);

    /**
     * Get details about the latest build result. Used to display the results of the test cases to the student: webapp/app/courses/results/result-deatil.html Used to generate the
     * interactive exercise instructions: webapp/app/editor/instructions/editor-instructions.components.js
     *
     * @param result the result for which to get details
     * @return List of automatic feedback by the continuous integration server. contains the test methods and their results:
     */
    public List<Feedback> getLatestBuildResultDetails(Result result);

    /**
     * Get the build logs of the latest CI build.
     *
     * @param buildPlanId to get the latest build logs
     * @return list of build log entries
     */
    public List<BuildLogEntry> getLatestBuildLogs(String buildPlanId);

    /**
     * Get the public URL to the build plan. Used for the "Go to Build Plan" button, if this feature is enabled for the exercise.
     *
     * @param participation participation for which to get the build plan URL
     * @return build plan url
     */
    public URL getBuildPlanWebUrl(ProgrammingExerciseParticipation participation);

    /**
     * Get the build artifact (JAR/WAR), if any, of the latest build
     *
     * @param participation participation for which to get the build artifact
     * @return the binary build artifact. Typically a JAR/WAR ResponseEntity.
     */
    public ResponseEntity retrieveLatestArtifact(ProgrammingExerciseParticipation participation);

    /**
     * Checks if the project with the given projectKey already exists
     *
     * @param projectKey to check if a project with this unique key already exists
     * @param projectName to check if a project with the same name already exists
     * @return true if the project exists, false otherwise
     */
    public String checkIfProjectExists(String projectKey, String projectName);
}
