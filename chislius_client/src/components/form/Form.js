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
                Submit
            </button>
        </div>
        <div className="form__btn-container">
            <button
                className='button form__btn'
                type='button'
                onClick={onSkip}
            >
                Skip
            </button>
        </div>
    </nav>)
}

export default Form