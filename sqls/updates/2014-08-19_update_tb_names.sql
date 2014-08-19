BEGIN;

ALTER TABLE usertb RENAME TO user;
ALTER TABLE partiride RENAME TO commutepartiride;
ALTER TABLE rideinfo RENAME TO commuteride;
ALTER TABLE topic RENAME TO commutetopic;
ALTER TABLE topicride RENAME TO commuteownerride;

COMMIT;