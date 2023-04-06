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
    #[generate_interface]
    pub fn to_string(&self) -> String {
        return format!("{} with {} sets of {} reps of {} lbs at {} \nWith the following note : {}\n",self.name, self.sets, self.reps, self.weight, self.date, self.notes);
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
            last_date : initial_date.clone(),
            entry_count : 0,
            log : Vec::new()
        }
    }
    #[generate_interface]
    pub fn add_entry(&mut self, entry: Entry) {
        self.log.push(entry);
        self.entry_count += 1;
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
}

pub struct History {
    exercise_count: i64,
    start_date: String,
    last_date: String,
    history_log: Vec<Exercise>

}

impl History {
    #[generate_interface(constructor)]
    pub fn new(start_date: String) -> History {
        Self {
            exercise_count: 0,
            start_date : start_date.clone(),
            last_date : start_date.clone(),
            history_log : Vec::new()
        }
    }
    #[generate_interface]
    pub fn add_exercise(&mut self, exercise: Exercise) {
        self.history_log.push(exercise);
        self.exercise_count += 1;
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
        let exercises_as_strings = self.history_log.iter().map(Exercise::get_log_string);
        let log_string = exercises_as_strings.collect::<Vec<String>>().join("\n");
        log_string
    }
}

