import React from 'react'
import './Form.css'

function Form({onSubmit, canSubmit, onSkip}) {
    return (<nav className="form">
        <div className="form__btn-container">
            <button
                className='button form__btn'
                type='button'
                onClick={onSubmit}
                disabled={!canSubmit}
            >
                Сварить
            </button>
        </div>
        <div className="form__btn-container">
            <button
                className='button form__btn'
                type='button'
                onClick={onSkip}
            >
                Пропустить
            </button>
        </div>
    </nav>)
}

export default Form