package com.example.api.trainer.`in`

import com.example.api.member.`in`.request.toCommand
import com.example.api.response.ApiResponse
import com.example.api.trainer.`in`.request.SignUpTrainerFromEmailRequest
import com.example.core.user.trainer.application.`in`.SignUpTrainerUseCase
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/trainer")
class TrainerController(
    private val trainerUseCase: SignUpTrainerUseCase,
) {
    @PostMapping("/sign-up/email")
    @Operation(
        summary = "email 을 사용한 회원가입",
    )
    fun signUpTrainerWithEmail(
        @RequestBody
        request: SignUpTrainerFromEmailRequest,
    ): ApiResponse<Unit> {
        trainerUseCase.signUpTrainerFromEmail(request.toCommand())
        return ApiResponse(data = Unit)
    }
}
