A contacts App listing all contacts on the device; allowing the user to call or text the contacts; with a search featur to help finding among them.

App Structure :

- this project makes use of MVVM(model-view-viewmodel) as its main architecture using jetpack's LiveData and ViewModel.
- Room is used for storing data in local database and fething the contacts later to show in the main list, searching among them etc.
- the contacts list data is loaded gradually, as the list reaches the end, new data is requested and showed; although, the jetpack's paging library isn't used in this project.
- app's UI states such as loading, search-mode etc. are represented as livedata objects so that a change in app's state can be observed from activites and fragments and the corresponding action will be taken.
