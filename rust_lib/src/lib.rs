mod java_glue;

pub use crate::java_glue::*;

use android_logger::Config;
use log::LevelFilter;
use rifgen::rifgen_attr::*;

pub struct RustLog;

impl RustLog {
    //set up logging
    #[generate_interface]
    pub fn initialise_logging() {
        #[cfg(target_os = "android")]
            android_logger::init_once(
            Config::default()
                .with_max_level(LevelFilter::Trace)
                .with_tag("Rust"),
        );
        log_panics::init();
        log::info!("Logging initialised from Rust");
    }
}

pub struct Entry {
    name: String,
    weight: i64,
    reps: i64,
    sets: i64,
    notes: String,
    date: String
}

impl Entry {
    #[generate_interface(constructor)]
    pub fn new(name: String,
               weight: i64,
               reps: i64,
               sets: i64,
               notes: String,
               date: String) -> Entry {
        Self {
            name,
            weight,
            reps,
            sets,
            notes,
            date,
        }
    }

    #[generate_interface(constructor)]
    pub fn from_string(s: String) -> Entry {
        let mut parts = s.split(";").map(|p| p.trim());
        let name = parts.next().unwrap().to_owned();
        let weight = parts.next().unwrap().parse().unwrap();
        let reps = parts.next().unwrap().parse().unwrap();
        let sets = parts.next().unwrap().parse().unwrap();
        let notes = parts.next().unwrap().to_owned();
        let date = parts.next().unwrap().to_owned();
        Entry::new(name, weight, reps, sets, notes, date)
    }

    #[generate_interface]
    pub fn get_name(&self) -> String {
        self.name.clone()
    }

    #[generate_interface]
    pub fn set_name(&mut self, name: String) {
        self.name = name;
    }

    #[generate_interface]
    pub fn get_weight(&self) -> i64 {
        self.weight
    }

    #[generate_interface]
    pub fn set_weight(&mut self, weight: i64) {
        self.weight = weight;
    }

    #[generate_interface]
    pub fn get_reps(&self) -> i64 {
        self.reps
    }

    #[generate_interface]
    pub fn set_reps(&mut self, reps: i64) {
        self.reps = reps;
    }

    #[generate_interface]
    pub fn get_sets(&self) -> i64 {
        self.sets
    }

    #[generate_interface]
    pub fn set_sets(&mut self, sets: i64) {
        self.sets = sets;
    }

    #[generate_interface]
    pub fn get_notes(&self) -> String {
        self.notes.clone()
    }

    #[generate_interface]
    pub fn set_notes(&mut self, notes: String) {
        self.notes = notes;
    }

    #[generate_interface]
    pub fn get_date(&self) -> String {
        self.date.clone()
    }

    #[generate_interface]
    pub fn set_date(&mut self, date: String) {
        self.date = date;
    }

    #[generate_interface]
    pub fn to_string(&self) -> String {
        return format!("{};{};{};{};{};{}",
                       self.name, self.weight, self.reps, self.sets, self.notes, self.date);
    }
    #[generate_interface]
    pub fn pretty_to_string(&self) -> String {
        return format!("{} with {} sets of {} reps of {} lbs at {} \nWith the following note : {}\n",self.name, self.sets, self.reps, self.weight, self.date, self.notes);
    }

    #[generate_interface]
    pub fn display_to_string(&self) -> String {
        return format!("{} | {} with {} sets of {} reps of {} lbs \n",
                       self.date, self.name, self.sets, self.reps, self.weight);
    }

}



pub struct Exercise {
    name: String,
    initial_weight: i64,
    initial_reps: i64,
    initial_sets: i64,
    initial_date: String,
    max_weight: i64,
    max_reps: i64,
    max_sets: i64,
    max_date: String,
    last_date: String,
    entry_count: i64,
    log: Vec<Entry>
}

impl Exercise {
    #[generate_interface(constructor)]
    pub fn new(name: String,
               initial_date: String) -> Exercise {
        Self {
            name,
            initial_weight : 0,
            initial_reps: 0,
            initial_sets: 0,
            initial_date : initial_date.clone(),
            max_weight : 0,
            max_reps : 0,
            max_sets: 0,
            max_date: initial_date.clone(),
            last_date : initial_date.clone(),
            entry_count : 0,
            log : Vec::new()
        }
    }

    #[generate_interface(constructor)]
    pub fn from_string(s: String) -> Exercise {
        let mut parts = s.split(",").map(|p| p.trim());
        let name = parts.next().unwrap().to_owned();
        let initial_weight = parts.next().unwrap().parse().unwrap();
        let initial_reps = parts.next().unwrap().parse().unwrap();
        let initial_sets = parts.next().unwrap().parse().unwrap();
        let initial_date = parts.next().unwrap().to_owned();
        let max_weight = parts.next().unwrap().parse().unwrap();
        let max_reps = parts.next().unwrap().parse().unwrap();
        let max_sets = parts.next().unwrap().parse().unwrap();
        let max_date = parts.next().unwrap().to_owned();
        let last_date = parts.next().unwrap().to_owned();
        let entry_count = parts.next().unwrap().parse().unwrap();
        let log_str = parts.next().unwrap().to_owned();
        let log_parts = log_str.split("\n");
        let mut log = Vec::new();
        for entry_str in log_parts {
            if !entry_str.is_empty() {
                log.push(Entry::from_string(entry_str.to_owned()));
            }
        }
        Exercise {
            name,
            initial_weight,
            initial_reps,
            initial_sets,
            initial_date,
            max_weight,
            max_reps,
            max_sets,
            max_date,
            last_date,
            entry_count,
            log,
        }
    }


    #[generate_interface]
    pub fn add_entry(&mut self, entry: Entry) {
        if self.initial_weight == 0 {
            self.initial_weight = entry.weight;
            self.initial_reps = entry.reps;
            self.initial_sets = entry.sets;
        }
        if self.max_weight <= entry.weight {
            self.max_weight = entry.weight;
            self.max_reps = entry.reps;
            self.max_sets = entry.sets;
            self.max_date = entry.date.clone();
        } else if self.max_weight == entry.weight && self.max_reps <= entry.reps {
            self.max_reps = entry.reps;
            self.max_sets = entry.sets;
            self.max_date = entry.date.clone();
        } else if self.max_weight == entry.weight && self.max_reps == entry.reps && self.max_sets <= entry.sets {
            self.max_sets = entry.sets;
            self.max_date = entry.date.clone();
        }
        self.last_date = entry.date.clone();
        self.log.push(entry);
        self.entry_count += 1;
    }

    #[generate_interface]
    pub fn get_name(&self) -> String {
        self.name.clone()
    }

    #[generate_interface]
    pub fn get_initial_weight(&self) -> i64 {
        self.initial_weight
    }

    #[generate_interface]
    pub fn get_initial_reps(&self) -> i64 {
        self.initial_reps
    }

    #[generate_interface]
    pub fn get_initial_sets(&self) -> i64 {
        self.initial_sets
    }

    #[generate_interface]
    pub fn get_initial_date(&self) -> String {
        self.initial_date.clone()
    }

    #[generate_interface]
    pub fn get_max_weight(&self) -> i64 {
        self.max_weight
    }

    #[generate_interface]
    pub fn get_max_reps(&self) -> i64 {
        self.max_reps
    }

    #[generate_interface]
    pub fn get_max_sets(&self) -> i64 {
        self.max_sets
    }

    #[generate_interface]
    pub fn get_max_date(&self) -> String {
        self.max_date.clone()
    }

    #[generate_interface]
    pub fn get_last_date(&self) -> String {
        self.last_date.clone()
    }

    #[generate_interface]
    pub fn get_entry_count(&self) -> i64 {
        self.entry_count
    }

    pub fn add_entries(&mut self, entries_string: String) {
        let mut parts = entries_string.split("\n");
        for entry_str in parts {
            if !entry_str.is_empty() {
                self.log.push(Entry::from_string(entry_str.to_owned()));
            }
        }
    }

    // pub fn print_log(&self) {
    //     if self.log.len() == 0 {
    //         println!("No Entries Found\n")
    //     }
    //     for entry in self.log.iter() {
    //         println!("{}", entry.to_string());
    //     };
    // }
    #[generate_interface]
    pub fn get_log_string(&self) -> String {
        if self.log.len() == 0 {
            return format!("No Entries Found\n")
        }
        let entries_as_strings = self.log.iter().map(Entry::to_string);
        let log_string = entries_as_strings.collect::<Vec<String>>().join("\n");
        log_string
    }

    #[generate_interface]
    pub fn to_string(&self) -> String {
        let log_str = self.get_log_string();
        format!("{},{},{},{},{},{},{},{},{},{},{},{}",
                self.name, self.initial_weight, self.initial_reps, self.initial_sets, self.initial_date, self.max_weight, self.max_reps, self.max_sets, self.max_date, self.last_date, self.entry_count, log_str)
    }

    #[generate_interface]
    pub fn display_to_string(&self) -> String {
        let mut result = String::from("");
        for entry in &self.log {
            result += &entry.display_to_string();
        }
        result
    }

    #[generate_interface]
    pub fn note_to_string(&self) -> String {
        let mut result = String::from("");
        for entry in &self.log {
            result += &entry.pretty_to_string();
        }
        result
    }

    #[generate_interface]
    pub fn pretty_to_string(&self) -> String {
        let mut result = format!("Exercise: {}\n", self.name);
        result += &format!(
            "Initial weight: {} lbs\nInitial reps: {}\nInitial sets: {}\nInitial date: {}\n",
            self.initial_weight, self.initial_reps, self.initial_sets, self.initial_date
        );
        result += &format!(
            "Max weight: {} lbs\nMax reps: {}\nMax sets: {}\nMax date: {}\nLast date: {}\n",
            self.max_weight, self.max_reps, self.max_sets, self.max_date,self.last_date
        );
        result += &format!("Entry count: {}\n\n", self.entry_count);
        result += "Exercise Log:\n";
        for entry in &self.log {
            result += &entry.pretty_to_string();
        }
        result
    }

    #[generate_interface]
    pub fn max_to_string(&self) -> String {
        let mut result = format!("Current max in {} is : ", self.name);
        result += &format!(
            "{} lbs, {} reps, {} sets on {}",
            self.max_weight, self.max_reps, self.max_sets, self.max_date
        );
        result
    }

    #[generate_interface]
    pub fn initial_to_string(&self) -> String {
        let mut result = format!("Initial max in {} is : ", self.name);
        result += &format!(
            "{} lbs, {} reps, {} sets on {}",
            self.initial_weight, self.initial_reps, self.initial_sets, self.initial_date
        );
        result
    }

    #[generate_interface]
    pub fn result_to_string(&self) -> String {
        let mut result = format!("Stats for {} is : \n\n", self.name);
        result += &format!(
            "Initial Max : {} lbs, {} reps, {} sets on {}\n",
            self.initial_weight, self.initial_reps, self.initial_sets, self.initial_date
        );
        result += &format!(
            "Current Max : {} lbs, {} reps, {} sets on {}\n",
            self.max_weight, self.max_reps, self.max_sets, self.max_date
        );
        result += &format!("Increase of {} lbs\n",self.max_weight - self.initial_weight);
        result
    }


}

pub struct History {
    exercise_count: i64,
    start_date: String,
    last_date: String,
    history_log: Vec<Exercise>

}

impl History {
    #[generate_interface(constructor)]
    pub fn new(start_date: String, exercise_count: i64) -> History {
        Self {
            exercise_count,
            start_date : start_date.clone(),
            last_date : start_date.clone(),
            history_log : Vec::new()
        }
    }
    #[generate_interface(constructor)]
    pub fn from_string(s: String) -> History {
        let mut parts = s.split(":").map(|p| p.trim());
        let exercise_count = parts.next().unwrap().parse().unwrap();
        let start_date = parts.next().unwrap().to_owned();
        let last_date = parts.next().unwrap().to_owned();
        let log_str = parts.next().unwrap().to_owned();
        let history_log_parts = log_str.split("\n\n");
        let mut history_log = Vec::new();
        for exercise_str in history_log_parts {
            if !exercise_str.is_empty() {
                history_log.push(Exercise::from_string(exercise_str.to_owned()));
            }
        }
        History {
            exercise_count,
            start_date,
            last_date,
            history_log,
        }
    }

    #[generate_interface]
    pub fn add_exercise(&mut self, exercise: Exercise) {
        if self.start_date == self.last_date {
            self.start_date = exercise.get_initial_date().clone();
        }
        self.last_date = exercise.get_last_date().clone();
        self.exercise_count += exercise.get_entry_count();
        //check if history_log already has that exercise and merge exercise logs
        if let Some(existing_exercise) = self.history_log.iter_mut().find(|e| e.name == exercise.name) {
            //Deep copy of exercise
            let temp: Exercise = Exercise::from_string(exercise.to_string());
            existing_exercise.add_entries(temp.get_log_string());
        } else {
            self.history_log.push(exercise);
        }
    }

    #[generate_interface]
    pub fn merge_history(&mut self, history: History) {
        self.last_date = history.get_last_date().clone();
        self.exercise_count += history.get_exercise_count();
        for exercise in history.history_log {
            self.add_exercise(exercise);
        }
    }

    #[generate_interface]
    pub fn get_exercise_count(&self) -> i64 {
        let mut exercise_count = self.exercise_count;
        if self.history_log.is_empty() {
            return exercise_count;
        }
        for exercise in &self.history_log {
            exercise_count += exercise.get_entry_count();
        }
        exercise_count
    }

    #[generate_interface]
    pub fn get_start_date(&self) -> String {
        self.start_date.clone()
    }

    #[generate_interface]
    pub fn get_last_date(&self) -> String {
        //TODO
        // might be wrong since I don't think it updates
        self.last_date.clone()
    }

    // pub fn print_log(&self) {
    //     if self.history_log.len() == 0 {
    //         println!("No Exercises Found\n")
    //     }
    //     for exercise in self.history_log.iter() {
    //         println!("{}", exercise.get_log_string());
    //     };
    // }
    #[generate_interface]
    pub fn get_log_string(&self) -> String {
        if self.history_log.len() == 0 {
            return format!("No Exercises Found\n")
        }
        let entries_as_strings = self.history_log.iter().map(Exercise::to_string);
        let log_string = entries_as_strings.collect::<Vec<String>>().join("\n\n");
        log_string
    }
    #[generate_interface]
    pub fn to_string(&self) -> String {
        let log_str = self.get_log_string();
        format!("{}:{}:{}:{}",
                self.exercise_count, self.start_date, self.last_date, log_str)
    }

    #[generate_interface]
    pub fn pretty_to_string(&self) -> String {
        let mut result = String::new();
        result.push_str(&format!("Exercise count: {}\n", self.exercise_count));
        result.push_str(&format!("Start date: {}\n", self.start_date));
        result.push_str(&format!("Last date: {}\n", self.last_date));
        result.push_str("History log:\n\n\n");

        if self.history_log.is_empty() {
            result.push_str("No exercises found.\n");
        } else {
            for exercise in self.history_log.iter() {
                result.push_str(&format!("{}\n", exercise.pretty_to_string()));
            }
        }

        result
    }

    #[generate_interface]
    pub fn display_to_string(&self) -> String {
        let mut result = String::new();

        if self.history_log.is_empty() {
            result.push_str("No exercises found.\n");
        } else {
            for exercise in self.history_log.iter().rev() {
                result.push_str(&format!("{}\n", exercise.display_to_string()));
            }
        }

        result
    }

    #[generate_interface]
    pub fn exercise_to_string(&self) -> String {
        let mut result = String::new();

        if self.history_log.is_empty() {
            result.push_str("No exercises found.\n");
        } else {
            for exercise in self.history_log.iter().rev() {
                result.push_str(&format!("{}\n", exercise.note_to_string()));
            }
        }
        result
    }

}

