package com.doro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jiage
 */
@Getter
@AllArgsConstructor
public class CountSnapshot {

    private String type;

    private long id;

    private long last;

    private long count;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CountSnapshot) {
            CountSnapshot compareObj = (CountSnapshot) obj;
            return this.getId() == compareObj.getId();
        }
        return false;
    }
}
