-- Sample scores for testing
-- Update existing enrollments with component scores and final exam scores

UPDATE enrollments SET 
    component_score_1 = 8.5,
    component_score_2 = 7.8,
    final_exam_score = 8.2
WHERE student_id = 1 AND course_id = 1;

UPDATE enrollments SET 
    component_score_1 = 9.0,
    component_score_2 = 8.5,
    final_exam_score = 8.8
WHERE student_id = 1 AND course_id = 2;

UPDATE enrollments SET 
    component_score_1 = 7.5,
    component_score_2 = 8.0,
    final_exam_score = 7.8
WHERE student_id = 1 AND course_id = 3;

-- Add more sample data for different students
UPDATE enrollments SET 
    component_score_1 = 8.0,
    component_score_2 = 7.5,
    final_exam_score = 8.1
WHERE student_id = 2 AND course_id = 1;

UPDATE enrollments SET 
    component_score_1 = 9.2,
    component_score_2 = 8.8,
    final_exam_score = 9.0
WHERE student_id = 2 AND course_id = 2;
