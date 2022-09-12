package org.bahmni.module.feedintegration.repository;

import org.bahmni.module.feedintegration.model.OpenMRSPatientFeedForCraterJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CronJobRepository extends JpaRepository<OpenMRSPatientFeedForCraterJob, Integer> {
}
