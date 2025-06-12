import {Container, Nav, Navbar, NavDropdown} from 'react-bootstrap'
import React, {useState} from 'react'
import LogoutModal from '../modal/LogoutModal'
import ChangeNameModal from '../modal/ChangeNameModal'
import Player from '../game/Player'
import ChangeAvatarModal from '../modal/ChangeAvatarModal'
import DeleteUserModal from '../modal/DeleteUserModal'

function MenuNavbar({player}) {
    const [showLogout, setShowLogout] = useState(false)
    const [showChangeName, setShowChangeName] = useState(false)
    const [showChangeAvatar, setShowChangeAvatar] = useState(false)
    const [showDeleteUser, setShowDeleteUser] = useState(false)

    return (<>
        <Navbar collapseOnSelect expand="lg" className="bg-white">
            <Container>
                <Navbar.Brand>
                    <Player player={player}/></Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
                <Navbar.Collapse className="justify-content-end" id="responsive-navbar-nav">
                    <Nav className="mb-auto">
                        <NavDropdown title="Настройки" id="collapsible-nav-dropdown">
                            <NavDropdown.Item onClick={() => setShowChangeName(true)}>Сменить имя</NavDropdown.Item>
                            <NavDropdown.Item onClick={() => setShowChangeAvatar(true)}>Сменить
                                аватар</NavDropdown.Item>
                            <NavDropdown.Item onClick={() => setShowDeleteUser(true)}>Удалить аккаунт</NavDropdown.Item>
                            <NavDropdown.Divider/>
                            <NavDropdown.Item onClick={() => setShowLogout(true)}>Выйти</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
        <LogoutModal
            show={showLogout}
            onHide={() => setShowLogout(false)}
        />
        <ChangeNameModal
            show={showChangeName}
            onHide={() => setShowChangeName(false)}
        />
        <ChangeAvatarModal
            show={showChangeAvatar}
            onHide={() => setShowChangeAvatar(false)}
        />
        <DeleteUserModal
            show={showDeleteUser}
            onHide={() => setShowDeleteUser(false)}
        />
    </>)
}

export default MenuNavbar