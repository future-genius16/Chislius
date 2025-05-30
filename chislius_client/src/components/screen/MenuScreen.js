import {Button, Container, Nav, Navbar, NavDropdown} from 'react-bootstrap'
import api from '../../client/ApiClient'
import React, {useState} from 'react'
import LogoutModal from '../user/LogoutModal'
import ChangeNameModal from '../user/ChangeNameModal'
import JoinPrivateModal from '../user/JoinPrivateModal'
import CreatePrivateModal from '../user/CreatePrivateModal'
import {useAuth} from '../../context/TokenContext'

function MenuScreen({player}) {
    const {token} = useAuth()

    const [showLogout, setShowLogout] = useState(false)
    const [showChangeName, setShowChangeName] = useState(false)
    // const [showChangeAvatar, setShowChangeAvatar] = useState(false);
    // const [showGameList, setShowGameList] = useState(false);
    const [showJoinPrivate, setShowJoinPrivate] = useState(false)
    const [showCreatePrivate, setShowCreatePrivate] = useState(false)

    const onJoinPublicClick = () => {
        api.joinPublicRoom(token).catch((err) => {
            console.log(err.message)
        })
    }

    return (<>
        <Navbar collapseOnSelect expand="lg" className="bg-body-secondary">
            <Container>
                <Navbar.Brand>Числиус</Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
                <Navbar.Collapse className="justify-content-end" id="responsive-navbar-nav">
                    <Nav className="mb-auto">
                        <Nav.Link>
                            <img
                                alt=""
                                src="/images/cards/5.svg"
                                width="30"
                                height="30"
                            />
                            <b>{player?.name}</b></Nav.Link>
                        <Nav.Link>Рейтинг <b>{player.rating}</b></Nav.Link>
                        <NavDropdown title="Настройки" id="collapsible-nav-dropdown">
                            <NavDropdown.Item onClick={() => setShowChangeName(true)}>Сменить имя</NavDropdown.Item>
                            <NavDropdown.Item>Сменить аватар</NavDropdown.Item>
                            <NavDropdown.Item>История игр</NavDropdown.Item>
                            <NavDropdown.Divider/>
                            <NavDropdown.Item onClick={() => setShowLogout(true)}>Выйти</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
        <Container>
            <h1>Главное меню</h1>
            <Button onClick={onJoinPublicClick}>Присоедениться к публиной комнате</Button>
            <Button onClick={() => setShowJoinPrivate(true)}>Присоедениться к приватной комнате</Button>
            <Button onClick={() => setShowCreatePrivate(true)}>Создать приватную комнату</Button>
        </Container>
        <LogoutModal
            show={showLogout}
            onHide={() => setShowLogout(false)}
        />
        <ChangeNameModal
            show={showChangeName}
            onHide={() => setShowChangeName(false)}
        />
        {/*<ChangeAvatarModal*/}
        {/*    show={showChangeAvatar}*/}
        {/*    onHide={() => setShowChangeAvatar(false)}*/}
        {/*    onSuccess={handleChangeAvatarSuccess}*/}
        {/*/>*/}
        {/*<GameListModal*/}
        {/*    show={showGameList}*/}
        {/*    onHide={() => setShowGameList(false)}*/}
        {/*/>*/}
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