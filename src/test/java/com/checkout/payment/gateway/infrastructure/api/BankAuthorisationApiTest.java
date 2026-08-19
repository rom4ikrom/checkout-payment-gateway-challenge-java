package com.checkout.payment.gateway.infrastructure.api;

import static com.checkout.payment.gateway.domain.utils.TestFixtures.validAuthorisePaymentRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse;
import com.checkout.payment.gateway.domain.model.values.AuthorisationCode;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BankAuthorisationApiTest {

  private BankAuthorisationApi underTest;

  @Mock
  private BankAuthorisationClient client;

  @BeforeEach
  void setup() {
    underTest = new BankAuthorisationApi(client);
  }

  @ParameterizedTest(name = "{index} returns {2}", quoteTextArguments = false)
  @MethodSource("authorisations")
  void respondsWithAuthorisationWhenClientIsUp(
      AuthorisePaymentHttpResponse httpResponse,
      AuthorisePaymentResponse expectedResponse,
      String testCaseName) {
    // given
    var request = validAuthorisePaymentRequest();
    var httpRequest = validHttpRequest();
    when(client.authorise(httpRequest)).thenReturn(httpResponse);

    // when
    var response = underTest.authorisePayment(request);

    // then
    assertThat(response).isEqualTo(expectedResponse);
  }

  @Test
  void respondsWithRejectedAuthorisationWhenExceptionIsThrownByClient() {
    // given
    var request = validAuthorisePaymentRequest();
    var httpRequest = validHttpRequest();
    when(client.authorise(httpRequest)).thenThrow(new RuntimeException());

    // when
    var response = underTest.authorisePayment(request);

    // then
    assertThat(response).isEqualTo(AuthorisePaymentResponse.Rejected.instance());
  }

  private static Stream<Arguments> authorisations() {
    var authorisationCode = "081d5380-cdb2-457e-94ca-f22904f8b665";
    return Stream.of(
        Arguments.of(
            new AuthorisePaymentHttpResponse(true, authorisationCode),
            new AuthorisePaymentResponse.Authorised(AuthorisationCode.of(authorisationCode)),
            "authorised authorisation"),
        Arguments.of(
            new AuthorisePaymentHttpResponse(false, ""),
            AuthorisePaymentResponse.Declined.instance(),
            "declined authorisation"
        )
    );
  }

  private static AuthorisePaymentHttpRequest validHttpRequest() {
    return AuthorisePaymentHttpRequest.builder()
        .cardNumber("4444333322221111")
        .expiryDate("09/2026")
        .currency("GBP")
        .amount(4201)
        .cvv("123")
        .build();
  }

}