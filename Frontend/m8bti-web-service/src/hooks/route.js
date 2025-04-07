import React, { useContext } from "react"
import { useRouter } from "next/router"
import { AuthContext } from "./auth"

const AuthRoute = ({ children }) => {
	const { currentUser } = useContext(AuthContext)
	const Router = useRouter()
	console.log(currentUser)
	if (currentUser) {
		return <>{children}</>
	} else {
		Router.push("/loginPage")
		return <></>
	}
}

export default AuthRoute