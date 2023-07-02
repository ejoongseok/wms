package leejoongseok.wms.common.workload;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WorkloadType {
    PICKING("집품"),
    PACKING("포장");

    private final String description;
}
