package com.taxapprf.domain.report

import com.taxapprf.domain.ReportRepository
import javax.inject.Inject

class SaveReportsFromExcelUseCase @Inject constructor(
    private val repository: ReportRepository
) {
    fun execute(storagePath: String) =
        repository.saveReportFromExcel(storagePath)
}