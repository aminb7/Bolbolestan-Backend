class SelectedCourses extends React.Component {
    constructor(props) {
        super(props);
        this.removeCourse = this.removeCourse.bind(this);
        this.getState = this.getState.bind(this);
        this.handleReset = this.handleReset.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentWillMount() {
        this.getState();
    }

    getState() {
        this.props.getState();
    }

    removeCourse(event, classCode) {
        this.props.removeCourse(event, classCode);
        this.getState();
    }

    handleReset(event) {
        this.props.handleReset(event);
        this.getState();
    }

    handleSubmit(event) {
        this.props.handleSubmit(event);
        this.getState();
    }

    render() {
        console.log('rendered');
        if (!this.props.isLoggedin)
            return (<h1>Please Login First!</h1>);

        return (
            <div className="row">
                <section className="workbook_section">
                    <fieldset className="workbook">
                        <legend className="legend_text">دروس انتخاب شده</legend>
                        <div className="table-wrapper-scroll-y my-custom-scrollbar">
                            <table className="table table1 table_text">
                                <thead>
                                <tr className="blue_text">
                                    <th></th>
                                    <th>وضعیت</th>
                                    <th>کد</th>
                                    <th>نام درس</th>
                                    <th>استاد</th>
                                    <th>واحد</th>
                                </tr>
                                </thead>
                                <tbody className="gray_text">
                                {Object.values(this.props.student.selectedCourses).map((selectedCourse) => (
                                    <tr>
                                        <td><input type="image" src="../images/icons/012-trash-bin.png" className="trash_logo" onClick={(e) => this.removeCourse(e, selectedCourse.course.code)} alt="trash"/></td>
                                        <td><span className={(selectedCourse.courseSelectionType == 'REGISTERED') ? (selectedCourse.state == 'FINALIZED' ? "green_bordered_text" : "blue_bordered_text") : "gray_bordered_text"}>
                                            {(selectedCourse.courseSelectionType == 'REGISTERED') ? (selectedCourse.state == 'FINALIZED' ? "ثبت شده" : "ثبت نهایی نشده") : "در انتظار"}</span></td>
                                        <td>{selectedCourse.course.classCode} - {selectedCourse.course.code}</td>
                                        <td>{selectedCourse.course.name}</td>
                                        <td>{selectedCourse.course.instructor}</td>
                                        <th className="blue_text">{selectedCourse.course.units}</th>
                                    </tr>))
                                }
                                </tbody>
                            </table>
                        </div>
                        <div className="line"></div>
                        <div className="row">
                            <p className="col-md-9 table_footer blue_text"><b>تعداد واحد ثبت شده: {this.props.student.selectedUnits}</b></p>
                            <input type="image" src="../images/icons/009-refresh-arrow.png" onClick={this.handleReset} className=" col-md-0 reload_icon"/>
                            <button type="submit" onClick={this.handleSubmit} className="col-md-2.5 final_submit blue_text">ثبت نهایی</button>
                        </div>
                    </fieldset>
                </section>
            </div>
        );
    }
}

class SearchBar extends React.Component {
    constructor(props) {
        super(props);
        this.handleFilter = this.handleFilter.bind(this);
        this.handleSearch = this.handleSearch.bind(this);
    }

    handleFilter(event) {
        this.props.handleFilter(event);
    }

    handleSearch(event) {
        this.props.handleSearch(event);
        this.props.getState();
    }

    render() {
        return (
            <div className="row search_bar">
                <input type="text" placeholder="نام درس" onChange={this.handleFilter} className="search_input col-md-8"/>
                <button type="submit" onClick={this.handleSearch} className="search_button col-md-3">جستجو 🔍</button>
            </div>
        );
    }
}

class FilteredCourses extends React.Component {
    constructor(props) {
        super(props);
        this.state = {index: 0};
        this.getState = this.getState.bind(this);
        this.handleAdd = this.handleAdd.bind(this);
        this.handleRowInfo = this.handleRowInfo.bind(this);
    }

    componentWillMount() {
        this.getState();
    }

    getState() {
        this.props.getState();
    }

    handleAdd(event, courseCode, classCode) {
        this.props.handleAdd(event, courseCode, classCode);
    }

    handleRowInfo(event, index) {
        this.setState(prevState => ({index: index}));
    }

    render() {
        return (
            <div className="row">
                <fieldset className="workbook2">
                    <legend className="legend_text2">دروس ارائه شده</legend>
                    <table className="table table2 table_text">
                        <thead>
                        <tr>
                            <th colSpan="8">
                                <button type="submit" onClick={(e) => this.props.handleType(e, 'all')} className="col-md-2 select_type">همه</button>
                                <button type="submit" onClick={(e) => this.props.handleType(e, 'Takhasosi')} className="col-md-2 select_type">اختصاصی</button>
                                <button type="submit" onClick={(e) => this.props.handleType(e, 'Asli')} className="col-md-2 select_type">اصلی</button>
                                <button type="submit" onClick={(e) => this.props.handleType(e, 'Paaye')} className="col-md-2 select_type">پایه</button>
                                <button type="submit" onClick={(e) => this.props.handleType(e, 'Umumi')} className="col-md-2 select_type">عمومی</button>
                            </th>
                        </tr>
                        <tr className="blue_text">
                            <th></th>
                            <th>کد</th>
                            <th>ظرفیت</th>
                            <th>نوع</th>
                            <th>نام درس</th>
                            <th>استاد</th>
                            <th>واحد</th>
                            <th>توضیحات</th>
                        </tr>
                        </thead>
                        <tbody className="gray_text">
                        {Object.values(this.props.courses).map((course, index) => (
                        <tr onClick={(e) => this.handleRowInfo(e, index)}>
                            <td><input type="image" src={course.numberOfStudents >= course.capacity ? "../images/icons/010-clock-circular-outline.png" : "../images/icons/011-add.png"} className={course.numberOfStudents >= course.capacity ? "clock_logo" : "add_logo"} onClick={(e) => this.handleAdd(e, course.code, course.classCode)} alt="add"/></td>
                            <td>{course.classCode} - {course.code}</td>
                            <td>{course.numberOfStudents}/{course.capacity}</td>
                            <td><span className={course.type == 'Umumi' ? "yellow_blocked_text" : course.type == 'Takhasosi' ? "blue_blocked_text" : course.type == 'Asli' ? "green_blocked_text" : "red_blocked_text"}>{course.type}</span></td>
                            <td>{course.name}</td>
                            <td>{course.instructor}</td>
                            <td className="bold_text">{course.units}</td>
                            {index == 0 ? <td className="last_bottom" rowSpan={Object.values(this.props.courses).length}>
                                <div className="triangle-border right" style={{bottom: (1100 -75 * this.state.index).toString() + 'px'}}>
                                    {Object.values(this.props.courses)[this.state.index].classTime.start} - {Object.values(this.props.courses)[this.state.index].classTime.end}
                                    <br/>
                                    {Object.values(this.props.courses)[this.state.index].classTime.days.join(' - ')}
                                    <br/><hr/><b>پیش نیازی‌ها</b><br/>
                                    {Object.values(this.props.courses)[this.state.index].prerequisites.join(' - ')}
                                    <br/><b>امتحان</b><br/>
                                    {new Date(Object.values(this.props.courses)[this.state.index].examTime.start).toLocaleTimeString(navigator.language, {hour: '2-digit', minute:'2-digit', hour12: false})} - {new Date(Object.values(this.props.courses)[this.state.index].examTime.end).toLocaleTimeString(navigator.language, {hour: '2-digit', minute:'2-digit', hour12: false})} - {new Date(Object.values(this.props.courses)[this.state.index].examTime.start).toLocaleDateString()}
                                </div>
                            </td> : ''}
                        </tr>))
                        }
                        </tbody>
                    </table>
                </fieldset>
            </div>
        );
    }
}

class CoursesPage extends React.Component {
    constructor(props) {
        super(props);
        var initialStudent = {selectedUnits: 0, selectedCourses: {}}
        this.state = {student: initialStudent, courses: {}, filter: '', isLoggedin: false};

        this.removeCourse = this.removeCourse.bind(this);
        this.getState = this.getState.bind(this);
        this.handleReset = this.handleReset.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);

        this.handleFilter = this.handleFilter.bind(this);
        this.handleSearch = this.handleSearch.bind(this);

        this.getState = this.getState.bind(this);
        this.handleAdd = this.handleAdd.bind(this);
    }

    componentWillMount() {
        this.getState();
    }

    getState() {
        fetch('loggedin_student')
            .then(resp => resp.json())
            .then(data => {
                this.setState(prevState => ({student: data, isLoggedin: true}));
            });

        fetch('filtered_courses')
            .then(resp => resp.json())
            .then(data => {
                this.setState(prevState => ({courses: data}));
            });
    }

    removeCourse(event, classCode) {
        event.preventDefault();
        const requestOptions = {
            method: 'DELETE',
        };
        fetch('remove_course?courseCode=' + classCode, requestOptions)
            .then(this.getState());
        this.getState();
    }

    handleReset(event) {
        event.preventDefault();
        const requestOptions = {
            method: 'DELETE',
        };
        fetch('reset', requestOptions)
            .then(this.getState());
        this.getState();
    }

    handleSubmit(event) {
        event.preventDefault();
        fetch('finalize_courses')
            .then(response => response.json())
            .then(data => {
                if (!data) alert('ثبت نهایی با شکست مواجه شد!');
                else alert('ثبت نهایی با موفقیت انجام شد!');
                this.getState();
            });
        this.getState();
    }

    handleFilter(event) {
        this.setState(prevState => ({filter: event.target.value}));
    }

    handleSearch(event) {
        event.preventDefault();
        var params = {
            "filter": this.state.filter
        };
        var queryString = Object.keys(params).map(function(key) {
            return key + '=' + params[key]
        }).join('&');
        const requestOptions = {
            method: 'POST',
            headers: {
                'content-length' : queryString.length,
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            body: queryString
        };
        fetch('search', requestOptions)
            .then(this.getState());
    }

    handleType(event, type) {
        event.preventDefault();
        var params = {
            "filter": type
        };
        var queryString = Object.keys(params).map(function(key) {
            return key + '=' + params[key]
        }).join('&');
        const requestOptions = {
            method: 'POST',
            headers: {
                'content-length' : queryString.length,
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            body: queryString
        };
        fetch('typesearch', requestOptions)
            .then(this.getState());
    }

    handleAdd(event, courseCode, classCode) {
        fetch('add_course?courseCode=' + courseCode + '&classCode=' + classCode)
            .then(resp => resp.json())
            .then(data => {
                if (!data) alert('امکان افزودن درس وجود ندارد!');
                this.getState();
            });
    }

    render() {
        return (
            <div className="container">
                <BolbolestanHeader src="courses" text1="خانه" text2="برنامه هفتگی" />
                <SelectedCourses removeCourse={this.removeCourse} getState={this.getState} handleReset={this.handleReset} handleSubmit={this.handleSubmit} student={this.state.student} isLoggedin={this.state.isLoggedin} />
                <SearchBar getState={this.getState} handleFilter={this.handleFilter} handleSearch={this.handleSearch} />
                <FilteredCourses getState={this.getState} handleType={this.handleType} handleAdd={this.handleAdd} courses={this.state.courses}/>
                <BolbolestanFooter />
            </div>
        );
    }
}