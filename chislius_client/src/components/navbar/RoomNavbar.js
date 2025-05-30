import {Container, Nav, Navbar} from 'react-bootstrap'
import React, {useState} from 'react'
import LeaveRoomModal from '../modal/LeaveRoomModal'
import {Lock, Star, StarFill, Unlock} from 'react-bootstrap-icons'

function RoomNavbar({player, data}) {
    const [showLeaveRoom, setShowLeaveRoom] = useState(false)

    return (<>
        <Navbar collapseOnSelect expand="lg" className="bg-white">
            <Container>
                <Navbar.Brand>
                    Комната {data.code} | {data.open && <Unlock className={'align-middle'}/>}
                    {!data.open && <Lock className={'bi-align-center'}/>} | <StarFill/>
                    {data.mode < 1 && <Star/>}{data.mode >= 1 && <StarFill/>}
                    {data.mode < 2 && <Star/>}{data.mode >= 2 && <StarFill/>}
                </Navbar.Brand>
                <Nav className="mb-auto">
                    <Nav.Link onClick={() => setShowLeaveRoom(true)}>Выйти из комнаты</Nav.Link>
                </Nav>
            </Container>
        </Navbar>
        <LeaveRoomModal show={showLeaveRoom} onHide={() => setShowLeaveRoom(false)}
        />
    </>)
}

export default RoomNavbar