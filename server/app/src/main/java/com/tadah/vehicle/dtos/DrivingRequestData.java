package com.tadah.vehicle.dtos;


import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 차량 운행 관련 데이터를 저장한다.
 */
@Generated
@Getter
@AllArgsConstructor
public final class DrivingRequestData {
    @NotNull(message = "위도가 입력되지 않았습니다.")
    @Range(min = -90, max = 90, message = "위도 범위를 벗어났습니다.")
    private final Double latitude;

    @NotNull(message = "경도가 입력되지 않았습니다.")
    @Range(min = -180, max = 180, message = "경도 범위를 벗어났습니다.")
    private final Double longitude;
}
