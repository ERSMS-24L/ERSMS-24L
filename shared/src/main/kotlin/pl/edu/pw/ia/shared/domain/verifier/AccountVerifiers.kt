package pl.edu.pw.ia.shared.domain.verifier

import java.util.UUID

interface AccountVerifier {

	fun accountExists(accountId: UUID): Boolean
}
