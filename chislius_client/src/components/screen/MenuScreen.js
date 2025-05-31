import {Button, Container, Nav, Navbar, NavDropdown} from 'react-bootstrap'
import api from '../../client/ApiClient'
import React, {useState} from 'react'
import LogoutModal from '../modal/LogoutModal'
import ChangeNameModal from '../modal/ChangeNameModal'
import JoinPrivateModal from '../modal/JoinPrivateModal'
import CreatePrivateModal from '../modal/CreatePrivateModal'
import {useAuth} from '../../context/TokenContext'
import MenuNavbar from '../navbar/MenuNavbar'

function MenuScreen({player}) {
    const {token} = useAuth()

    const [showJoinPrivate, setShowJoinPrivate] = useState(false)
    const [showCreatePrivate, setShowCreatePrivate] = useState(false)

    const onJoinPublicClick = () => {
        api.joinPublicRoom(token).catch((err) => {
            console.log(err.message)
        })
    }

    return (<>
        <MenuNavbar player={player}/>
        <Container className={'mt-5'}>
            <h1><b>Главное меню</b></h1>
            <div className="d-grid gap-3 mt-4">
                <Button
                    size="lg"
                    className="mb-3 w-100" onClick={onJoinPublicClick}>Присоединиться к публичной комнате</Button>
                <Button
                    size="lg"
                    className="mb-3 w-100" onClick={() => setShowJoinPrivate(true)}>Присоединиться к приватной
                    комнате</Button>
                <Button
                    size="lg"
                    className="mb-3 w-100" onClick={() => setShowCreatePrivate(true)}>Создать приватную комнату</Button>
            </div>
        </Container>
        <JoinPrivateModal
            show={showJoinPrivate}
            onHide={() => setShowJoinPrivate(false)}
        />
        <CreatePrivateModal
            show={showCreatePrivate}
            onHide={() => setShowCreatePrivate(false)}
        />
    </>)
}

export default MenuScreen