package ntou.notesharedevbackend.searchModule.entity;

import org.springframework.data.domain.*;

import java.util.*;

public class Pages {
    private List<?> items;
    private int totalPages;

    public Pages(List<?> items, int totalPages) {
        setItems(items);
        setTotalPages(totalPages);
    }

    public List<?> getItems() {
        return items;
    }

    public void setItems(List<?> items) {
        this.items = items;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
