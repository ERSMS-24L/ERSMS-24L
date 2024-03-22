package pl.edu.pw.ia.shared.infrastructure.verifier

import java.util.UUID
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway
import org.springframework.stereotype.Service
import pl.edu.pw.ia.shared.domain.query.FindAccountByIdQuery
import pl.edu.pw.ia.shared.domain.verifier.AccountVerifier
import pl.edu.pw.ia.shared.domain.view.AccountView

@Service
class AccountVerifierImpl(
	val reactorQueryGateway: ReactorQueryGateway,
) : AccountVerifier {

	override fun accountExists(accountId: UUID): Boolean =
		reactorQueryGateway.query(FindAccountByIdQuery(accountId), AccountView::class.java)
			.blockOptional()
			.isPresent
}
