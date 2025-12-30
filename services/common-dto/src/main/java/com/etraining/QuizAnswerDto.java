package com.etraining;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerDto {
    private String answerText;
    private boolean isCorrect;
}
