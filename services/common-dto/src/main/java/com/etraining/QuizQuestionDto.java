package com.etraining;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestionDto {
    private String questionText;
    private List<QuizAnswerDto> answers;
}
