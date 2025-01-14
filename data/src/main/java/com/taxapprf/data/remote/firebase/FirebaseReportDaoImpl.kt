package com.taxapprf.data.remote.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.taxapprf.data.remote.firebase.dao.FirebaseReportDao
import com.taxapprf.domain.FirebasePathModel
import com.taxapprf.data.remote.firebase.model.FirebaseReportModel
import com.taxapprf.data.safeCall
import com.taxapprf.domain.report.SaveReportModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await


class FirebaseReportDaoImpl(
    private val fb: FirebaseAPI,
) : FirebaseReportDao {
    override fun getReports(accountKey: String) =
        callbackFlow {
            safeCall {
                val reference = fb.getReportsPath(accountKey)

                val callback = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val reports = snapshot.children.mapNotNull {
                            it.getValue(FirebaseReportModel::class.java)?.toReportModel()
                        }

                        trySendBlocking(reports)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                }

                reference.addValueEventListener(callback)

                awaitClose { reference.removeEventListener(callback) }
            }
        }

    override suspend fun addReport(saveReportModel: SaveReportModel) {
        TODO("Not yet implemented")
    }

    override suspend fun updateReport(saveReportModel: SaveReportModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReport(firebasePathModel: FirebasePathModel) {
        safeCall {
            fb.getReportPath(firebasePathModel)
                .setValue(null)
                .await()

            fb.getTransactionsPath(firebasePathModel)
                .setValue(null)
                .await()
        }
    }

    override suspend fun getReportTax(firebasePathModel: FirebasePathModel) =
        fb.getReportPath(firebasePathModel)
            .get()
            .await()
            .getValue(FirebaseReportModel::class.java)

    override suspend fun saveReportTax(
        firebasePathModel: FirebasePathModel,
        firebaseReportModel: FirebaseReportModel
    ) {
        fb.getReportPath(firebasePathModel)
            .setValue(firebaseReportModel)
            .await()
    }
}