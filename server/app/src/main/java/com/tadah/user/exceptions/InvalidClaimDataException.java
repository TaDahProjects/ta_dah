package com.tadah.user.exceptions;

/**
 * 유효하지 않은 속성 정보를 이용하여 토큰을 생성하려는 경우 던져짐
 * 해당 예외의 경우 던져지지 않도록 api 서버가 미리 검증을 수행해야함
 * 만약 예외가 던져진 경우, api 서버의 속성 정보 검증 기능이 잘못된 것이므로 스프링 어플리케이션을 종료시켜야함
 */
// TODO InvalidClaimDataException 던져진 경우 어플리케이션 종료 기능 작성
// TODO log 추가
public class InvalidClaimDataException extends RuntimeException {
}
