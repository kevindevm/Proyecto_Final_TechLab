package com.techlab.kevin.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BulkProductResponseDTO {

  private List<BulkDTO> created;
  private List<BulkDTO> failed;
  private String timestamp;

  public BulkProductResponseDTO(List<BulkDTO> created, List<BulkDTO> failed) {
    this.created = created;
    this.failed = failed;
    this.timestamp = LocalDateTime.now().toString();
  }
}