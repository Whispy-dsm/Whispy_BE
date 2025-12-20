-- V2__rename_batch_tables_to_upper.sql
-- Windows(대소문자 구분 없음) 호환성을 위해 임시 이름(TEMP)을 거쳐서 변경합니다.

-- 1. JOB_INSTANCE
RENAME TABLE tbl_batch_job_instance TO tbl_batch_job_instance_TEMP;
RENAME TABLE tbl_batch_job_instance_TEMP TO tbl_batch_JOB_INSTANCE;

-- 2. JOB_EXECUTION
RENAME TABLE tbl_batch_job_execution TO tbl_batch_job_execution_TEMP;
RENAME TABLE tbl_batch_job_execution_TEMP TO tbl_batch_JOB_EXECUTION;

-- 3. JOB_EXECUTION_PARAMS
RENAME TABLE tbl_batch_job_execution_params TO tbl_batch_job_execution_params_TEMP;
RENAME TABLE tbl_batch_job_execution_params_TEMP TO tbl_batch_JOB_EXECUTION_PARAMS;

-- 4. STEP_EXECUTION
RENAME TABLE tbl_batch_step_execution TO tbl_batch_step_execution_TEMP;
RENAME TABLE tbl_batch_step_execution_TEMP TO tbl_batch_STEP_EXECUTION;

-- 5. STEP_EXECUTION_CONTEXT
RENAME TABLE tbl_batch_step_execution_context TO tbl_batch_step_execution_context_TEMP;
RENAME TABLE tbl_batch_step_execution_context_TEMP TO tbl_batch_STEP_EXECUTION_CONTEXT;

-- 6. JOB_EXECUTION_CONTEXT
RENAME TABLE tbl_batch_job_execution_context TO tbl_batch_job_execution_context_TEMP;
RENAME TABLE tbl_batch_job_execution_context_TEMP TO tbl_batch_JOB_EXECUTION_CONTEXT;

-- 7. 시퀀스 테이블
RENAME TABLE tbl_batch_step_execution_seq TO tbl_batch_step_execution_seq_TEMP;
RENAME TABLE tbl_batch_step_execution_seq_TEMP TO tbl_batch_STEP_EXECUTION_SEQ;

RENAME TABLE tbl_batch_job_execution_seq TO tbl_batch_job_execution_seq_TEMP;
RENAME TABLE tbl_batch_job_execution_seq_TEMP TO tbl_batch_JOB_EXECUTION_SEQ;

RENAME TABLE tbl_batch_job_seq TO tbl_batch_job_seq_TEMP;
RENAME TABLE tbl_batch_job_seq_TEMP TO tbl_batch_JOB_SEQ;